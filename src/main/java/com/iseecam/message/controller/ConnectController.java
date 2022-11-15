package com.iseecam.message.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.UserModel;
import com.iseecam.message.model.request.ConnectRequest;
import com.iseecam.message.model.request.PublicConnectRequest;
import com.iseecam.message.service.ConnectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConnectController {

    private final ConnectService connectService;

    @PostMapping("/connect")
    public UserModel connect(@AuthenticationPrincipal Jwt principal, @RequestBody ConnectRequest request) {
        return connectService.connect(principal.getClaim("username"), request);
    }

    @PostMapping("/public/connect")
    public UserModel connect(@RequestBody PublicConnectRequest request) {
        return connectService.connect(request.getUsername(), request);
    }

}
