package com.iseecam.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.iseecam.message.domain.RoomEntity;
import com.iseecam.message.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomEntity get(String room) {
        return roomRepository.load(room);
    }

    public RoomEntity create(RoomEntity room) {
        room.setCreationTime(new Date().getTime());
        return roomRepository.save(room);
    }

    public RoomEntity update(RoomEntity room) {
        return roomRepository.update(room);
    }

    public RoomEntity getOrCreate(String room) {
        RoomEntity roomEntity = get(room);
        if (Objects.isNull(roomEntity)) {
            roomEntity = create(RoomEntity.builder()
                    .users(new ArrayList<>())
                    .room(room)
                    .build());
        }
        return roomEntity;
    }

}
