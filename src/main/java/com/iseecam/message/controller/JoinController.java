package com.iseecam.message.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.request.JoinRequest;
import com.iseecam.message.model.response.JoinResponse;
import com.iseecam.message.service.JoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public JoinResponse join(@AuthenticationPrincipal Jwt principal, @RequestBody JoinRequest request) {
        return joinService.join(principal.getClaim("username") ,request);
    }

}
