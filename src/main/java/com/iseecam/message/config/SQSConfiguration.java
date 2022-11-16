package com.iseecam.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SQSConfiguration {

    @Value("${sqs.accessKey}")
    private String sqsAccessKey;
    
    @Value("${sqs.secretKey}")
    private String sqsSecretKey;

    @Bean
    public AmazonSQS buildAmazonSQS() {
            return AmazonSQSClientBuilder
                            .standard()
                            .withRegion(Regions.EU_WEST_1)
                            .withCredentials(
                                            new AWSStaticCredentialsProvider(
                                                            new BasicAWSCredentials(
                                                                            sqsAccessKey,
                                                                            sqsSecretKey)))
                            .build();
    }
}
