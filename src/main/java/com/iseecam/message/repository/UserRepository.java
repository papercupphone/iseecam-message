package com.iseecam.message.repository;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.iseecam.message.domain.UserEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public UserEntity load(String username) {
        return dynamoDBMapper.load(UserEntity.class, username);
    }

    public UserEntity save(UserEntity username) {
        dynamoDBMapper.save(username);
        return username;
    }

    public void deleteByUsername(String username) {
        dynamoDBMapper.delete(deleteExpression(username));
    }

    public UserEntity update(UserEntity user) {
        dynamoDBMapper.save(user, updateExpression(user));
        return user;
    }

    private DynamoDBSaveExpression updateExpression(UserEntity entity) {
        return new DynamoDBSaveExpression()
                .withExpectedEntry("username",
                        new ExpectedAttributeValue(
                                new AttributeValue().withS(entity.getUsername())));
    }

    public DynamoDBDeleteExpression deleteExpression(String username) {
        return new DynamoDBDeleteExpression()
                .withExpectedEntry("username",
                        new ExpectedAttributeValue(
                                new AttributeValue().withS(username)));
    }

}
