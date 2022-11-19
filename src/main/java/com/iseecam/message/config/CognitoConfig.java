package com.iseecam.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoConfig {

    @Value("${cognito.accessKey}")
    private String cognitoAccessKey;

    @Value("${cognito.secretKey}")
    private String cognitoSecretKey;

    @Value("${aws.region}")
    String region;

    @Bean
    public AWSCognitoIdentityProvider buildAmazonCognito() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(cognitoAccessKey,
                        cognitoSecretKey)))
                .withRegion(region).build();
    }
}
