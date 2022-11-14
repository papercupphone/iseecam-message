package com.iseecam.message;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;

public class CognitoAuthenticationHelper {

    private static final AWSCredentials credentials = new BasicAWSCredentials("AKIA4B7GKBMAYBGDGM44", "EJY3D/4Xz4ZxdzIzp754xbD1Aibq2V8FZyR1SI9g");
    private static final AWSCognitoIdentityProvider cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("eu-west-1").build();

    public static String getToken() {
        Map initialParams = new HashMap();
        initialParams.put("USERNAME", "ekin");
        initialParams.put("PASSWORD", "A123ee.!");

        AdminInitiateAuthRequest initialRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withAuthParameters(initialParams)
                .withClientId("4pljkbatljbjc17rh6ei9ejh0e")
                .withUserPoolId("eu-west-1_2B7xVU4hZ");

        AdminInitiateAuthResult initialResponse = cognitoClient.adminInitiateAuth(initialRequest);
        return initialResponse.getAuthenticationResult().getAccessToken();
    }

}
