package com.iseecam.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

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
                                                                Regions.EU_WEST_1.getName()))
                                .withCredentials(
                                                new AWSStaticCredentialsProvider(
                                                                new BasicAWSCredentials(
                                                                                dynamoAccessKey,
                                                                                dynamoSecretKey)))
                                .build();
        }

}
