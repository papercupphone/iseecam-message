package com.iseecam.message.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iseecam.message.model.MessageModel;
import com.iseecam.message.model.request.PageRequest;
import com.iseecam.message.model.response.PageResponse;
import com.iseecam.message.service.MessageService;
import com.iseecam.message.service.SocketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SocketService socketService;

    @PostMapping("/message")
    public MessageModel create(@AuthenticationPrincipal Jwt principal, @RequestBody MessageModel message) {
        return messageService.create(principal.getClaim("username"), message);
    }

    @PostMapping("/public/message")
    public void create(@RequestBody MessageModel message) {
        socketService.sendAllPeers(message);
    }

    @GetMapping("/message/{id}")
    public MessageModel get(@PathVariable("id") String id) {
        return messageService.getById(id);
    }

    @DeleteMapping("/admin/message/{id}")
    public void delete(@PathVariable("id") String id) {
        messageService.deleteById(id);
    }

    @GetMapping("/message/room/{room}")
    public List<MessageModel> getAllByRoom(@PathVariable("room") String room) {
        return messageService.getAllByRoom(room);
    }

    @DeleteMapping("/admin/message/room/{room}")
    public void deleteByRoomId(@PathVariable("room") String room) {
        messageService.deleteAllByRoom(room);
    }

    @GetMapping("/message/room/{room}/page")
    public PageResponse<MessageModel> getAllByRoomPage(@RequestBody PageRequest pageRequest,
            @PathVariable("room") String room) {
        return messageService.getAllByRoom(room, pageRequest);
    }

}
