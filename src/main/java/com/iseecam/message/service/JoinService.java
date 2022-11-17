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

    public JoinResponse privateJoin(String username, JoinRequest request) {
        if (Objects.nonNull(username) && !username.equals(request.getUsername())) {
            throw new AuthorizationException("Username mismatch");
        }
        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room)) {
            return join(room, request);
        } else {
            room = create(true, request);
            return JoinResponse.builder()
                    .room(room.getRoom())
                    .users(room.getUsers())
                    .build();
        }
    }

    public JoinResponse publicJoin(JoinRequest request) {
        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room)) {
            if (room.isSecure()) {
                throw new AuthorizationException("Room is private");
            } else {
                return join(room, request);
            }
        } else {
            room = create(false, request);
            return JoinResponse.builder()
                    .room(room.getRoom())
                    .users(room.getUsers())
                    .build();
        }
    }

    private RoomEntity create(boolean secure, JoinRequest request) {
        return roomService.create(RoomEntity.builder()
                .room(request.getRoom())
                .secure(secure)
                .users(Arrays.asList(request.getUsername()))
                .build());
    }

    private JoinResponse join(RoomEntity room, JoinRequest request) {
        if ((Objects.nonNull(room.getUsers()) && !room.getUsers().isEmpty())) {
            if (!room.getUsers().contains(request.getUsername())) {
                room.getUsers().add(request.getUsername());
            }
        } else {
            room.setUsers(Arrays.asList(request.getUsername()));
        }
        roomService.update(room);
        socketService.sendAllPeers(MessageModel.builder()
                .type(MessageType.SYSTEM.name())
                .room(room.getRoom())
                .message(request.getUsername() + " joined the room")
                .build());
        return JoinResponse.builder()
                .room(room.getRoom())
                .users(room.getUsers())
                .build();
    }

}
