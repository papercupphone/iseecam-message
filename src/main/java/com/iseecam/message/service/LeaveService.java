package com.iseecam.message.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.iseecam.message.domain.RoomEntity;
import com.iseecam.message.exception.AuthorizationException;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.model.request.LeaveRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final RoomService roomService;

    public String leave(String username, LeaveRequest request) {
        if (Objects.nonNull(username) && !username.equals(request.getUsername())) {
            throw new AuthorizationException("Username mismatch");
        }

        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room) && Objects.nonNull(room.getUsers()) && !room.getUsers().isEmpty()) {
            room.getUsers().remove(request.getUsername());
            roomService.update(room);
            return "OK";
        } else {
            throw new ValidationException("Room not found");
        }
    }

}
