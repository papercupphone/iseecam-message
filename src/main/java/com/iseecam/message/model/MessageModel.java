package com.iseecam.message.model;

import com.iseecam.message.domain.MessageEntity;

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
public class MessageModel {

    private String id;
    private String message;
    private String room;
    private String sender;
    private String type;
    private boolean read;
    private long creationTime;
    private long updateTime;

    public MessageEntity toEntity() {
        return MessageEntity.builder()
                .id(id)
                .message(message)
                .room(room)
                .sender(sender)
                .creationTime(creationTime)
                .updateTime(updateTime)
                .read(read)
                .type(type)
                .build();
    }

    public static MessageModel toModel(MessageEntity messageEntity) {
        return MessageModel.builder()
                .id(messageEntity.getId())
                .message(messageEntity.getMessage())
                .room(messageEntity.getRoom())
                .sender(messageEntity.getSender())
                .creationTime(messageEntity.getCreationTime())
                .updateTime(messageEntity.getUpdateTime())
                .read(messageEntity.isRead())
                .type(messageEntity.getType())
                .build();
    }

}
