package com.iseecam.message.service;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    public String privateLeave(String username, LeaveRequest request) {
        if (Objects.nonNull(username) && !username.equals(request.getUsername())) {
            throw new AuthorizationException("Username mismatch");
        }
        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room)) {
            return leave(room, request);
        } else {
            throw new ValidationException("Room does not exist");
        }
    }

    public String publicLeave(LeaveRequest request) {
        RoomEntity room = roomService.get(request.getRoom());
        if (Objects.nonNull(room)) {
            if (room.isSecure()) {
                throw new AuthorizationException("Room is private");
            } else {
                return leave(room, request);
            }
        } else {
            throw new ValidationException("Room does not exist");
        }
    }

    public String leave(RoomEntity room, LeaveRequest request) {
        if (Objects.nonNull(room.getUsers()) && !room.getUsers().isEmpty()) {
            Predicate<String> equalsUsername = user -> user.equals(request.getUsername());
            Consumer<String> removeUser = user -> room.getUsers().remove(user);
            room.getUsers().stream().filter(equalsUsername).findFirst().ifPresent(removeUser);
            room.getUsers().remove(request.getUsername());
            roomService.update(room);
            return "OK";
        } else {
            throw new ValidationException("User not found");
        }
    }

}
