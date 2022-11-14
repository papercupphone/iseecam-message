package com.iseecam.message.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.request.LeaveRequest;
import com.iseecam.message.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/leave")
    public String join(@AuthenticationPrincipal Jwt principal, @RequestBody LeaveRequest request) {
        return leaveService.leave(principal.getClaim("username"), request);
    }

}
