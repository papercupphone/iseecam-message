package com.iseecam.message.service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionResult;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iseecam.message.domain.RoomEntity;
import com.iseecam.message.domain.UserEntity;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.model.MessageModel;

@Service
public class SocketService {

    private static ObjectMapper mapper = new ObjectMapper();

    private final AmazonApiGatewayManagementApi amazonApiGatewayManagementApi;
    private final RoomService roomService;
    private final UserService userService;

    public SocketService(@Value("${apigateway.endpoint}") String endpoint,
            @Value("${aws.region}") String region,
            @Value("${apigateway.stage}") String stage,
            @Value("${apigateway.accessKey}") String accessKey,
            @Value("${apigateway.secretKey}") String secretKey,
            RoomService roomService,
            UserService userService) {
        amazonApiGatewayManagementApi = AmazonApiGatewayManagementApiClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                accessKey,
                                secretKey)))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint + "/" + stage, region))
                .build();
        this.roomService = roomService;
        this.userService = userService;
    }

    public PostToConnectionResult send(String connectionId, MessageModel message) throws JsonProcessingException {
        PostToConnectionRequest request = new PostToConnectionRequest();
        request.withConnectionId(connectionId);
        request.withData(ByteBuffer.wrap(mapper.writeValueAsBytes(message)));
        return amazonApiGatewayManagementApi.postToConnection(request);
    }

    public boolean checkConnection(String connection) {
        try {
            GetConnectionResult getConnectionResult = amazonApiGatewayManagementApi
                    .getConnection(new GetConnectionRequest().withConnectionId(connection));
            return Objects.nonNull(getConnectionResult);
        } catch (Exception e) {
            return false;
        }
    }

    public void sendAllPeers(MessageModel message) {
        RoomEntity room = roomService.getOrCreate(message.getRoom());
        if (Objects.nonNull(room) && Objects.nonNull(room.getUsers()) && !room.getUsers().isEmpty()) {
            List<String> users = room.getUsers();
            users.forEach((username) -> {
                UserEntity user = userService.load(username);
                if (Objects.nonNull(user) && Objects.nonNull(user.getConnectionId())) {
                    try {
                        if (checkConnection(user.getConnectionId())) {
                            send(user.getConnectionId(), message);
                        }
                    } catch (JsonProcessingException e) {
                        throw new ValidationException("socket.send_failed");
                    }
                }
            });
        }
    }

}
