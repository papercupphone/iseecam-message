package com.iseecam.message.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.request.UserConfirmSignUpRequest;
import com.iseecam.message.model.request.UserGetRefreshToken;
import com.iseecam.message.model.request.UserSignInRequest;
import com.iseecam.message.model.request.UserSignUpRequest;
import com.iseecam.message.model.response.UserConfirmSignUpResponse;
import com.iseecam.message.model.response.UserSignInResponse;
import com.iseecam.message.model.response.UserSignUpResponse;
import com.iseecam.message.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/auth/signin")
    public UserSignInResponse signIn(@RequestBody UserSignInRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/auth/confirm")
    public UserConfirmSignUpResponse confirm(@RequestBody UserConfirmSignUpRequest request) {
        return authService.confirm(request);
    }

    @PostMapping("/auth/refresh")
    public UserSignInResponse refresh(@RequestBody UserGetRefreshToken request) {
        return authService.refresh(request);
    }
}
