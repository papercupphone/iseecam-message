package com.iseecam.message.model;

import java.util.List;

import com.iseecam.message.domain.RoomEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomModel {

    private String room;
    private long updateTime;
    private long creationTime;
    private boolean secure;
    private List<String> users;

    public RoomEntity toEntity() {
        return RoomEntity.builder()
                .room(room)
                .updateTime(updateTime)
                .creationTime(creationTime)
                .secure(secure)
                .users(users)
                .build();
    }

    public static RoomModel toModel(RoomEntity roomEntity) {
        return RoomModel.builder()
                .room(roomEntity.getRoom())
                .updateTime(roomEntity.getUpdateTime())
                .creationTime(roomEntity.getCreationTime())
                .secure(roomEntity.isSecure())
                .users(roomEntity.getUsers())
                .build();
    }
}
