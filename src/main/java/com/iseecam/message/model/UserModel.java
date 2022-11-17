package com.iseecam.message.model;

import com.iseecam.message.domain.UserEntity;

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
public class UserModel {

    private String username;
    private String connectionId;
    private long updateTime;
    private long creationTime;
    private boolean guest;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .username(username)
                .connectionId(connectionId)
                .updateTime(updateTime)
                .creationTime(creationTime)
                .guest(guest)
                .build();
    }

    public static UserModel toModel(UserEntity userEntity) {
        return UserModel.builder()
                .username(userEntity.getUsername())
                .connectionId(userEntity.getConnectionId())
                .updateTime(userEntity.getUpdateTime())
                .creationTime(userEntity.getCreationTime())
                .guest(userEntity.isGuest())
                .build();
    }
}
