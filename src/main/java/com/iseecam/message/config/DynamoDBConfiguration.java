package com.iseecam.message.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfiguration {

        @Value("${dynamo.accessKey}")
        private String dynamoAccessKey;
        @Value("${dynamo.secretKey}")
        private String dynamoSecretKey;

        @Bean
        public DynamoDBMapper dynamoDBMapper() {
                return new DynamoDBMapper(buildAmazonDynamoDB());
        }

        private AmazonDynamoDB buildAmazonDynamoDB() {
                return AmazonDynamoDBClientBuilder
                                .standard()
                                .withEndpointConfiguration(
                                                new AwsClientBuilder.EndpointConfiguration(
                                                                "dynamodb.eu-west-1.amazonaws.com",
                                                                "eu-west-1"))
                                .withCredentials(
                                                new AWSStaticCredentialsProvider(
                                                                new BasicAWSCredentials(
                                                                                dynamoAccessKey,
                                                                                dynamoSecretKey)))
                                .build();
        }

}
