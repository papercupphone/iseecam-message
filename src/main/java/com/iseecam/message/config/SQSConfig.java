package com.iseecam.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SQSConfig {

    @Value("${sqs.accessKey}")
    private String sqsAccessKey;
    
    @Value("${sqs.secretKey}")
    private String sqsSecretKey;

    @Value("${aws.region}")
    String region;

    @Bean
    public AmazonSQS buildAmazonSQS() {
            return AmazonSQSClientBuilder
                            .standard()
                            .withRegion(region)
                            .withCredentials(
                                            new AWSStaticCredentialsProvider(
                                                            new BasicAWSCredentials(
                                                                            sqsAccessKey,
                                                                            sqsSecretKey)))
                            .build();
    }
}
