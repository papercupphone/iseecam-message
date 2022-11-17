package com.iseecam.message.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
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
@DynamoDBTable(tableName = "iseecam_user")
public class UserEntity {

    @DynamoDBHashKey
    private String username;

    @DynamoDBAttribute
    private String connectionId;

    @DynamoDBAttribute
    private long updateTime;

    @DynamoDBAttribute
    private boolean guest;
    
    @DynamoDBAttribute
    private long creationTime;

}
