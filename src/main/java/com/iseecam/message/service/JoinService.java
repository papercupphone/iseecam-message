package com.iseecam.message.service;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.iseecam.message.domain.RoomEntity;
import com.iseecam.message.enums.MessageType;
import com.iseecam.message.exception.AuthorizationException;
import com.iseecam.message.model.MessageModel;
import com.iseecam.message.model.request.JoinRequest;
import com.iseecam.message.model.response.JoinResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final RoomService roomService;
    private final SocketService socketService;

    public JoinResponse join(String username, JoinRequest request) {
        if (Objects.nonNull(username) && !username.equals(request.getUsername())) {
            throw new AuthorizationException("Username mismatch");
        }
        return join(request);
    }

    public JoinResponse join(JoinRequest request) {
        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room)
                && (Objects.nonNull(room.getUsers()) && !room.getUsers().isEmpty())) {
            if (!room.getUsers().contains(request.getUsername())) {
                room.getUsers().add(request.getUsername());
                roomService.update(room);
                socketService.sendAllPeers(MessageModel.builder()
                        .type(MessageType.SYSTEM.name())
                        .room(room.getRoom())
                        .message(request.getUsername() + " joined the room")
                        .build());
            }
        } else {
            room = roomService.create(RoomEntity.builder()
                    .room(request.getRoom())
                    .users(Arrays.asList(request.getUsername()))
                    .build());
        }
        return JoinResponse.builder()
                .room(room.getRoom())
                .users(room.getUsers())
                .build();
    }

}
