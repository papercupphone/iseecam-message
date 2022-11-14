package com.iseecam.message.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    
    @RequestMapping(path = "/test/ping", method = RequestMethod.GET)
    public Map<String, String> ping() {
        Map<String, String> pong = new HashMap<>();
        pong.put("pong", "Hello, World!");
        return pong;
    }

    @RequestMapping(path = "/private/me", method = RequestMethod.GET)
    public Map<String, String> me(@AuthenticationPrincipal Jwt principal) {
        String currentPrincipalName = principal.getClaim("username");
        Map<String, String> response = new HashMap<>();
        response.put("name", currentPrincipalName);
        return response;
    }

    @RequestMapping(path = "/admin/me", method = RequestMethod.GET)
    public Map<String, String> admin(@AuthenticationPrincipal Jwt principal) {
        String currentPrincipalName = principal.getClaim("username");
        Map<String, String> response = new HashMap<>();
        response.put("name", currentPrincipalName);
        return response;
    }
    
}
