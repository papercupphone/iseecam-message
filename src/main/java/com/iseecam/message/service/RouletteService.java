package com.iseecam.message.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.iseecam.message.model.request.JoinRequest;
import com.iseecam.message.model.request.RouletteJoinRequest;
import com.iseecam.message.model.response.RouletteJoinResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final AmazonSQS sqs;
    private final JoinService joinService;
    private static final String ROOM_SUFFIX = "-roulette-room";

    @Value("${sqs.endpoint}")
    private String endpoint;

    public RouletteJoinResponse join(RouletteJoinRequest request) {
        String room = getOrCreateAvailableRoom(request);
        join(room, request.getUsername(), request.getConnectionId());
        return RouletteJoinResponse.builder()
                .room(room)
                .message("joined " + room)
                .build();
    }

    private String getOrCreateAvailableRoom(RouletteJoinRequest request) {
        ReceiveMessageResult receiveMessageResult = receive();
        String room;
        if (Objects.nonNull(receiveMessageResult) && !receiveMessageResult.getMessages().isEmpty()) {
            room = receiveMessageResult.getMessages().get(0).getBody();
            sqs.deleteMessage(endpoint, receiveMessageResult.getMessages().get(0).getReceiptHandle());
        } else {
            room = request.getUsername() + ROOM_SUFFIX + System.currentTimeMillis();
            send(room);
        }
        return room;
    }

    private ReceiveMessageResult receive() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(endpoint)
                .withMaxNumberOfMessages(1);
        return sqs.receiveMessage(receiveMessageRequest);
    }

    private void send(String room) {
        SendMessageRequest sendMessageFifoQueue = new SendMessageRequest()
                .withQueueUrl(endpoint)
                .withMessageBody(room)
                .withMessageGroupId("roulette-group-1");
        sqs.sendMessage(sendMessageFifoQueue);
    }

    private void join(String room, String username, String connectionId) {
        joinService.join(JoinRequest.builder()
                .connectionId(connectionId)
                .room(room)
                .username(username)
                .build());
    }
}
