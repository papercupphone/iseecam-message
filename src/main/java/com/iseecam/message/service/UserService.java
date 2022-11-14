package com.iseecam.message.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.iseecam.message.domain.UserEntity;
import com.iseecam.message.model.request.ConnectRequest;
import com.iseecam.message.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity load(String username) {
        return userRepository.load(username);
    }

    public UserEntity create(String username, ConnectRequest user) {
        return userRepository.save(UserEntity.builder()
                .username(username)
                .connectionId(user.getConnectionId())
                .creationTime(new Date().getTime())
                .build());
    }

    public UserEntity update(UserEntity entity) {
        return userRepository.update(entity);
    }
}
