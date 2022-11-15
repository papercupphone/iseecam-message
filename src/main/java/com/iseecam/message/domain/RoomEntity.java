package com.iseecam.message.domain;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "iseecam_room")
public class RoomEntity {

    @DynamoDBHashKey
    private String room;

    @DynamoDBAttribute
    private long updateTime;

    @DynamoDBAttribute
    private long creationTime;

    @DynamoDBRangeKey
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "user-count-index")
    private int userCount;

    @DynamoDBAttribute
    private List<String> users;

}
