package com.iseecam.message.e2e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iseecam.message.MainTest;
import com.iseecam.message.StreamLambdaHandler;
import com.iseecam.message.model.request.UserGetRefreshToken;
import com.iseecam.message.model.request.UserSignInRequest;
import com.iseecam.message.model.request.UserSignUpRequest;
import com.iseecam.message.model.response.UserSignInResponse;

public class AuthControllerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        MainTest.handler = new StreamLambdaHandler();
        MainTest.lambdaContext = new MockLambdaContext();
    }

    @Test
    public void signUpTest() throws JsonProcessingException {
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .username("ekin1")
                .password("password1E.!")
                .email("ekngnnc@gmail.com")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/auth/signup", "POST")
                .body(mapper.writeValueAsString(userSignUpRequest))
                .header("Content-Type", "application/json")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = MainTest.readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusCode());

        assertFalse(response.isBase64Encoded());
    }

    /*
     * @Test
     * public void signUpConfirmTest() throws JsonProcessingException {
     * UserConfirmSignUpRequest userConfirmSignUpRequest =
     * UserConfirmSignUpRequest.builder()
     * .username("ekin1")
     * .code("148683")
     * .build();
     * InputStream requestStream = new AwsProxyRequestBuilder("/auth/confirm",
     * "POST")
     * .body(mapper.writeValueAsString(userConfirmSignUpRequest))
     * .header("Content-Type", "application/json")
     * .buildStream();
     * ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
     * 
     * MainTest.handle(requestStream, responseStream);
     * 
     * AwsProxyResponse response = MainTest.readResponse(responseStream);
     * assertNotNull(response);
     * assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
     * 
     * assertFalse(response.isBase64Encoded());
     * }
     */

    @Test
    public void signInTest() throws JsonProcessingException {
        UserSignInRequest signInRequest = UserSignInRequest.builder()
                .username("ekin1")
                .password("password1E.!")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/auth/signin", "POST")
                .body(mapper.writeValueAsString(signInRequest))
                .header("Content-Type", "application/json")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = MainTest.readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertFalse(response.isBase64Encoded());
    }

    @Test
    public void refreshTokenTest() throws JsonProcessingException {
        UserSignInRequest signInRequest = UserSignInRequest.builder()
                .username("ekin1")
                .password("password1E.!")
                .build();
        InputStream requestStream = new AwsProxyRequestBuilder("/auth/signin", "POST")
                .body(mapper.writeValueAsString(signInRequest))
                .header("Content-Type", "application/json")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        MainTest.handle(requestStream, responseStream);

        AwsProxyResponse response = MainTest.readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        UserSignInResponse userSignInResponse = mapper.readValue(response.getBody(),
                new TypeReference<UserSignInResponse>() {
                });

        UserGetRefreshToken userGetRefreshToken = UserGetRefreshToken.builder()
                .refreshToken(userSignInResponse.getRefreshToken())
                .build();
        InputStream requestStream2 = new AwsProxyRequestBuilder("/auth/refresh", "POST")
                .body(mapper.writeValueAsString(userGetRefreshToken))
                .header("Content-Type", "application/json")
                .buildStream();
        ByteArrayOutputStream responseStream2 = new ByteArrayOutputStream();

        MainTest.handle(requestStream2, responseStream2);

        AwsProxyResponse response2 = MainTest.readResponse(responseStream);
        assertNotNull(response2);
        UserSignInResponse userSignInResponse2 = mapper.readValue(response.getBody(),
                new TypeReference<UserSignInResponse>() {
                });
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertNotNull(userSignInResponse2.getAccessToken());
    }

}
