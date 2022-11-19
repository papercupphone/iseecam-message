package com.iseecam.message.service;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.model.request.UserConfirmSignUpRequest;
import com.iseecam.message.model.request.UserGetRefreshToken;
import com.iseecam.message.model.request.UserSignInRequest;
import com.iseecam.message.model.request.UserSignUpRequest;
import com.iseecam.message.model.response.UserConfirmSignUpResponse;
import com.iseecam.message.model.response.UserSignInResponse;
import com.iseecam.message.model.response.UserSignUpResponse;

@Service
public class AuthService {

    @Value("${cognito.clientId}")
    private String clientId;

    @Value("${cognito.userPoolId}")
    private String userPoolId;

    @Autowired
    private AWSCognitoIdentityProvider cognitoClient;

    public UserSignUpResponse signUp(UserSignUpRequest request) {
        request.validate();
        SignUpRequest signUpRequest = new SignUpRequest().withClientId(clientId)
                .withUsername(request.getUsername())
                .withPassword(request.getPassword())
                .withUserAttributes(
                        new AttributeType()
                                .withName("email")
                                .withValue(request.getEmail()));
        try {
            cognitoClient.signUp(signUpRequest);
        } catch (InvalidPasswordException e) {
            throw new ValidationException("auth.invalid_password");
        } catch (UsernameExistsException e) {
            throw new ValidationException("auth.username_exists");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ValidationException("auth.signup_failed");
        }

        return UserSignUpResponse.builder().message("auth.signup_success").build();
    }

    public UserSignInResponse signIn(UserSignInRequest request) {
        request.validate();
        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPoolId)
                .withClientId(clientId)
                .withAuthParameters(new LinkedHashMap<String, String>() {
                    {
                        put("USERNAME", request.getUsername());
                        put("PASSWORD", request.getPassword());
                    }
                });
        try {
            AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);
            AuthenticationResultType resultType = authResult.getAuthenticationResult();
            return UserSignInResponse.builder()
                    .accessToken(resultType.getAccessToken())
                    .refreshToken(resultType.getRefreshToken())
                    .idToken(resultType.getIdToken())
                    .expiresIn(resultType.getExpiresIn())
                    .message("auth.sign_in_success")
                    .build();
        } catch (UserNotConfirmedException e) {
            throw new ValidationException("auth.sign_in_failed");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ValidationException("auth.signin_failed");
        }
    }

    public UserConfirmSignUpResponse confirm(UserConfirmSignUpRequest request) {
        request.validate();
        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
                .withClientId(clientId)
                .withUsername(request.getUsername())
                .withConfirmationCode(request.getCode());
        try {
            ConfirmSignUpResult confirmSignUpResult = cognitoClient.confirmSignUp(confirmSignUpRequest);
            return UserConfirmSignUpResponse.builder().message("auth.confirm_success").build();
        } catch (Exception e) {
            System.out.println(e);
            throw new ValidationException("auth.confirm_failed");
        }
    }

    public UserSignInResponse refresh(UserGetRefreshToken request) {
        request.validate();
        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest().withAuthFlow(AuthFlowType.REFRESH_TOKEN)
                .withClientId(clientId).withAuthParameters(new LinkedHashMap<String, String>() {
                    {
                        put("REFRESH_TOKEN", request.getRefreshToken());
                    }
                });

        try {
            InitiateAuthResult authResult = cognitoClient.initiateAuth(initiateAuthRequest);
            AuthenticationResultType resultType = authResult.getAuthenticationResult();
            return UserSignInResponse.builder()
                    .accessToken(resultType.getAccessToken())
                    .refreshToken(resultType.getRefreshToken())
                    .idToken(resultType.getIdToken())
                    .expiresIn(resultType.getExpiresIn())
                    .message("auth.refresh_success")
                    .build();
        } catch (Exception e) {
            System.out.println(e);
            throw new ValidationException("auth.refresh_failed");
        }
    }

}
