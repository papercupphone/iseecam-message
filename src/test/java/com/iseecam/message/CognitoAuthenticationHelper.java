package com.iseecam.message;

import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;

public class CognitoAuthenticationHelper {

    private static final AWSCredentials credentials = new BasicAWSCredentials("AKIA4B7GKBMAYBGDGM44",
            "EJY3D/4Xz4ZxdzIzp754xbD1Aibq2V8FZyR1SI9g");
    private static final AWSCognitoIdentityProvider cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("eu-west-1").build();

    public static String getToken() {
        HashMap<String, String> initialParams = new HashMap();
        initialParams.put("USERNAME", "ekin");
        initialParams.put("PASSWORD", "A123ee.!");

        AdminInitiateAuthRequest initialRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withAuthParameters(initialParams)
                .withClientId("5o3p0gl8pli383dsi2hq1s7b5m")
                .withUserPoolId("eu-west-1_HkXutiKkq");

        AdminInitiateAuthResult initialResponse = cognitoClient.adminInitiateAuth(initialRequest);
        return initialResponse.getAuthenticationResult().getAccessToken();
    }

}
