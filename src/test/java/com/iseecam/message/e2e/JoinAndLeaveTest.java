package com.iseecam.message.e2e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.annotation.Order;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iseecam.message.CognitoAuthenticationHelper;
import com.iseecam.message.MainTest;
import com.iseecam.message.StreamLambdaHandler;
import com.iseecam.message.model.request.JoinRequest;
import com.iseecam.message.model.request.LeaveRequest;
import com.iseecam.message.model.response.JoinResponse;
 
public class JoinAndLeaveTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        MainTest.handler = new StreamLambdaHandler();
        MainTest.lambdaContext = new MockLambdaContext();
    }

    @Test
    @Order(1)
    public void joinTest() throws IOException {
        JoinRequest message = JoinRequest.builder()
                .username("ekin")
                .room("room")
                .connectionId("connectionId")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/join", HttpMethod.POST)
                // add body
                .body(mapper.writeValueAsString(message))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = mapper.readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody());
        JoinResponse user = mapper.readValue(response.getBody(),
                new TypeReference<JoinResponse>() {
                });
        assertNotNull(user);
    }

    @Test
    @Order(1)
    public void leaveTest() throws JsonProcessingException {
        LeaveRequest message = LeaveRequest.builder()
                .room("room")
                .username("ekin")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/leave", HttpMethod.POST)
                // add body
                .body(mapper.writeValueAsString(message))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = MainTest.readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody().equals("OK"));
    }

    @Test
    public void joinTestWithFalseUser() throws IOException {
        JoinRequest message = JoinRequest.builder()
                .username("ekin2")
                .room("room")
                .connectionId("connectionId")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/join", HttpMethod.POST)
                // add body
                .body(mapper.writeValueAsString(message))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = mapper.readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusCode());
    }
}
