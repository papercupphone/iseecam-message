package com.iseecam.message.repository;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.iseecam.message.domain.RoomEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public RoomEntity save(RoomEntity room) {
        dynamoDBMapper.save(room);
        return room;
    }

    public RoomEntity load(String room) {
        return dynamoDBMapper.load(RoomEntity.class, room);
    }

    public RoomEntity update(RoomEntity message) {
        dynamoDBMapper.save(message, updateExpression(message));
        return message;
    }

    private DynamoDBSaveExpression updateExpression(RoomEntity entity) {
        return new DynamoDBSaveExpression()
                .withExpectedEntry("room",
                        new ExpectedAttributeValue(
                                new AttributeValue().withS(entity.getRoom())));
    }

}
