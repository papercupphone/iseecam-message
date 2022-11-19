package com.iseecam.message.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.iseecam.message.domain.UserEntity;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.model.UserModel;
import com.iseecam.message.model.request.ConnectRequest;
import com.iseecam.message.model.request.PublicConnectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConnectService {

    private final UserService userService;

    public UserModel connect(String username, ConnectRequest request) {
        UserEntity userEntity = userService.load(username);
        if (Objects.nonNull(userEntity)) {
            userEntity.setConnectionId(request.getConnectionId());
            userEntity.setUpdateTime(new Date().getTime());
            userService.update(userEntity);
            return UserModel.toModel(userEntity);
        } else {
            return UserModel.toModel(userService.create(username, request));
        }
    }

    public UserModel publicConnect(PublicConnectRequest request) {
        if (Objects.nonNull(request.getUsername())) {
            return connect(request.getUsername(), request);
        } else {
            throw new ValidationException("connect.request_username_is_null");
        }
    }

}
