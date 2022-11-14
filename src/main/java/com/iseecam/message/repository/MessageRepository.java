package com.iseecam.message.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.iseecam.message.domain.MessageEntity;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.key.CompositeKey;
import com.iseecam.message.key.MessageCompositeKey;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public MessageEntity save(MessageEntity message) {
        dynamoDBMapper.save(message);
        return message;
    }

    public MessageEntity getMessageById(String id) {
        DynamoDBQueryExpression<MessageEntity> queryExpression = queryByIdExpression(id,
                MessageEntity.class);
        List<MessageEntity> messages = dynamoDBMapper.query(MessageEntity.class, queryExpression);
        if (messages.stream().findFirst().isPresent()) {
            return messages.stream().findFirst().get();
        } else {
            throw new ValidationException("Message not found");
        }
    }

    private <E> DynamoDBQueryExpression<E> queryByIdExpression(String id, Class<E> clazz) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(id));
        return new DynamoDBQueryExpression<E>()
                .withKeyConditionExpression("id = :val1")
                .withExpressionAttributeValues(eav);
    }

    public void deleteById(String id) {
        dynamoDBMapper.delete(deleteExpression(id));
    }

    public MessageEntity update(MessageEntity message) {
        dynamoDBMapper.save(message, updateExpression(message));
        return message;
    }

    private DynamoDBSaveExpression updateExpression(MessageEntity baseEntity) {
        return new DynamoDBSaveExpression()
                .withExpectedEntry("id",
                        new ExpectedAttributeValue(
                                new AttributeValue().withS(baseEntity.getId())));
    }

    public DynamoDBDeleteExpression deleteExpression(String id) {
        return new DynamoDBDeleteExpression()
                .withExpectedEntry("id",
                        new ExpectedAttributeValue(
                                new AttributeValue().withS(id)));
    }

    public List<MessageEntity> getAllByRoom(String room) {
        DynamoDBQueryExpression<MessageEntity> dynamoDBQueryExpression = queryAllByField("room", room,
                MessageEntity.class);
        return dynamoDBMapper.query(MessageEntity.class, dynamoDBQueryExpression);
    }

    private <E> DynamoDBQueryExpression<E> queryAllByField(String field, String value, Class<E> clazz) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(value));
        DynamoDBQueryExpression<E> scanExpression = new DynamoDBQueryExpression<E>()
                .withIndexName(field + "-index")
                .withConsistentRead(false)
                .withKeyConditionExpression(field + " = :val1").withExpressionAttributeValues(eav);
        return scanExpression;
    }

    public QueryResultPage<MessageEntity> getAllByRoom(String room, int size, MessageCompositeKey messageCompositeKey) {
        return dynamoDBMapper.queryPage(MessageEntity.class,
                queryAllByField("room", room, size, messageCompositeKey, MessageEntity.class));
    }

    private <E> DynamoDBQueryExpression<E> queryAllByField(String field, String value, int size,
            CompositeKey messageCompositeKey, Class<E> clazz) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(value));
        DynamoDBQueryExpression<E> queryExpression = new DynamoDBQueryExpression<E>()
                .withExpressionAttributeValues(eav)
                .withIndexName(field + "-index")
                .withConsistentRead(false)
                .withKeyConditionExpression(field + " = :val1")
                .withExclusiveStartKey(
                        Objects.nonNull(messageCompositeKey)
                                ? messageCompositeKey.toAttributeValueMap()
                                : null)
                .withLimit(size);
        return queryExpression;
    }

    public void deleteAllByRoom(String room) {
        dynamoDBMapper.batchDelete(getAllByRoom(room));
    }

}
