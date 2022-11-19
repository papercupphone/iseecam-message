package com.iseecam.message.e2e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iseecam.message.CognitoAuthenticationHelper;
import com.iseecam.message.MainTest;
import com.iseecam.message.StreamLambdaHandler;
import com.iseecam.message.model.MessageModel;
import com.iseecam.message.model.request.PageRequest;
import com.iseecam.message.model.response.PageResponse;

public class MessageControllerTest {

        private static ObjectMapper mapper = new ObjectMapper();

        @BeforeClass
        public static void setUp() {
                MainTest.handler = new StreamLambdaHandler();
                MainTest.lambdaContext = new MockLambdaContext();
        }

        @Test
        public void createMessageTest() throws JsonProcessingException {
                MessageModel message = MessageModel.builder()
                                .message("Hello World")
                                .room("ekin2")
                                .sender("ekin")
                                .build();
                InputStream requestStream = new AwsProxyRequestBuilder("/message", HttpMethod.POST)
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

                assertFalse(response.isBase64Encoded());

                assertTrue(response.getBody().contains("message"));
                assertTrue(response.getBody().contains(message.getMessage()));

                assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
                assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE)
                                .startsWith(MediaType.APPLICATION_JSON));
        }

        @Test
        public void createPublicMessageTest() throws JsonProcessingException {
                MessageModel message = MessageModel.builder()
                                .message("Hello World")
                                .room("room23")
                                .sender("user6")
                                .build();
                InputStream requestStream = new AwsProxyRequestBuilder("/public/message", HttpMethod.POST)
                                // add body
                                .body(mapper.writeValueAsString(message))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .buildStream();
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

                MainTest.handle(requestStream, responseStream);

                AwsProxyResponse response = MainTest.readResponse(responseStream);
                assertNotNull(response);
                assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

                assertFalse(response.isBase64Encoded());
        }

        @Test
        public void createMessageWithFalseUser() throws JsonProcessingException {
                MessageModel message = MessageModel.builder()
                                .message("Hello World")
                                .room("test")
                                .sender("user1")
                                .build();
                InputStream requestStream = new AwsProxyRequestBuilder("/message", HttpMethod.POST)
                                // add body
                                .body(mapper.writeValueAsString(message))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                                .buildStream();
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

                MainTest.handle(requestStream, responseStream);

                AwsProxyResponse response = MainTest.readResponse(responseStream);
                assertNotNull(response);
                assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusCode());

                assertFalse(response.isBase64Encoded());

                assertTrue(response.getBody().contains("message"));

                assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
                assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE)
                                .startsWith(MediaType.APPLICATION_JSON));
        }

        @Test
        public void listMessageWithRoomNameUser() throws JsonProcessingException {
                InputStream requestStream = new AwsProxyRequestBuilder("/message/room/test1", HttpMethod.GET)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                                .buildStream();
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

                MainTest.handle(requestStream, responseStream);

                AwsProxyResponse response = MainTest.readResponse(responseStream);
                assertNotNull(response);
                assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
                List<MessageModel> messageModels = mapper.readValue(response.getBody(),
                                new TypeReference<List<MessageModel>>() {
                                });
                assertNotNull(messageModels);
        }

        @Test
        public void deleteMessageWithRoomTest() throws JsonProcessingException {
                InputStream requestStream = new AwsProxyRequestBuilder("/admin/message/room/test1", HttpMethod.DELETE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                                .buildStream();
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

                MainTest.handle(requestStream, responseStream);

                AwsProxyResponse response = MainTest.readResponse(responseStream);
                assertNotNull(response);
                assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        }

        @Test
        public void getMessagesWithRoomPaginationTest() throws JsonProcessingException {
                InputStream requestStream = new AwsProxyRequestBuilder("/message/room/test/page", HttpMethod.GET)
                                .body(mapper.writeValueAsString(PageRequest.builder()
                                                .size(2)
                                                .build()))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                                .buildStream();

                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

                MainTest.handle(requestStream, responseStream);

                AwsProxyResponse response = MainTest.readResponse(responseStream);
                assertNotNull(response);
                assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

                PageResponse pageResponse = mapper.readValue(response.getBody(),
                                new TypeReference<PageResponse>() {
                                });

                System.out.println(pageResponse.getContents());

                InputStream requestStream2 = new AwsProxyRequestBuilder("/message/room/test/page", HttpMethod.GET)
                                .body(mapper.writeValueAsString(PageRequest.builder()
                                                .size(2)
                                                .compositeKey(pageResponse.getMessageCompositeKey())
                                                .build()))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CognitoAuthenticationHelper.getToken())
                                .buildStream();

                ByteArrayOutputStream responseStream2 = new ByteArrayOutputStream();

                MainTest.handle(requestStream2, responseStream2);

                AwsProxyResponse response2 = MainTest.readResponse(responseStream2);
                assertNotNull(response2);
                assertEquals(Response.Status.OK.getStatusCode(), response2.getStatusCode());

                PageResponse pageResponse2 = mapper.readValue(response2.getBody(),
                                new TypeReference<PageResponse>() {
                                });

                System.out.println(pageResponse.getContents().size());
                System.out.println(pageResponse2.getContents().size());
        }

}
