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

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iseecam.message.CognitoAuthenticationHelper;
import com.iseecam.message.MainTest;
import com.iseecam.message.StreamLambdaHandler;
import com.iseecam.message.model.UserModel;
import com.iseecam.message.model.request.ConnectRequest;
import com.iseecam.message.model.request.PublicConnectRequest;

public class ConnectControllerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        MainTest.handler = new StreamLambdaHandler();
        MainTest.lambdaContext = new MockLambdaContext();
    }

    @Test
    public void connectTest() throws IOException {
        ConnectRequest message = ConnectRequest.builder()
                .connectionId("connectionId")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/connect", HttpMethod.POST)
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
        UserModel user = mapper.readValue(response.getBody(),
                new TypeReference<UserModel>() {
                });
        assertNotNull(user);
    }

    @Test
    public void publicConnectTest() throws IOException {
        PublicConnectRequest message = new PublicConnectRequest();
        message.setUsername("identifier");
        message.setConnectionId("connectionId");
        InputStream requestStream = new AwsProxyRequestBuilder("/public/connect", HttpMethod.POST)
                // add body
                .body(mapper.writeValueAsString(message))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = mapper.readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody());
        UserModel user = mapper.readValue(response.getBody(),
                new TypeReference<UserModel>() {
                });
        assertNotNull(user);
    }
    
}
