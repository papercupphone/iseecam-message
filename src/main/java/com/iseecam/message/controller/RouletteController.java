package com.iseecam.message.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.request.RouletteJoinRequest;
import com.iseecam.message.model.response.RouletteJoinResponse;
import com.iseecam.message.service.RouletteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @PostMapping("/public/roulette/join")
    public RouletteJoinResponse join(@RequestBody RouletteJoinRequest request) {
        return rouletteService.join(request);
    }

}
