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
import com.iseecam.message.MainTest;
import com.iseecam.message.StreamLambdaHandler;
import com.iseecam.message.model.request.RouletteJoinRequest;
import com.iseecam.message.model.response.RouletteJoinResponse;

public class RouletteControllerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        MainTest.handler = new StreamLambdaHandler();
        MainTest.lambdaContext = new MockLambdaContext();
    }

    @Test
    public void joinTest() throws IOException {
        RouletteJoinRequest request = RouletteJoinRequest.builder()
                .connectionId("connectionId")
                .username("ekin")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/public/roulette/join", HttpMethod.POST)
                // add body
                .body(mapper.writeValueAsString(request))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = mapper.readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody());
        RouletteJoinResponse responseModel = mapper.readValue(response.getBody(),
                new TypeReference<RouletteJoinResponse>() {
                });
        assertNotNull(responseModel);
    }

}
