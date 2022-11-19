package com.iseecam.message.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.iseecam.message.domain.MessageEntity;
import com.iseecam.message.enums.MessageType;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.key.MessageCompositeKey;
import com.iseecam.message.model.MessageModel;
import com.iseecam.message.model.request.PageRequest;
import com.iseecam.message.model.response.PageResponse;
import com.iseecam.message.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SocketService socketService;

    public MessageModel create(String username, MessageModel message) {
        if (Objects.nonNull(username) && username.equals(message.getSender())) {
            MessageEntity messageEntity = message.toEntity();
            messageEntity.setCreationTime(new Date().getTime());
            messageEntity.setType(MessageType.TEXT.name());
            MessageModel messageModel = MessageModel.toModel(messageEntity);
            socketService.sendAllPeers(messageModel);
            messageRepository.save(messageEntity);
            return messageModel;
        } else {
            throw new ValidationException("message.sender_not_match");
        }
    }

    public void deleteById(String id) {
        messageRepository.deleteById(id);
    }

    public MessageModel update(MessageModel message) {
        MessageEntity messageEntity = message.toEntity();
        messageEntity.setUpdateTime(new Date().getTime());
        messageRepository.update(messageEntity);
        return MessageModel.toModel(messageEntity);
    }

    public MessageModel getById(String id) {
        return MessageModel.toModel(messageRepository.getMessageById(id));
    }

    public List<MessageModel> getAllByRoom(String room) {
        // TODO control if user is in room
        return messageRepository.getAllByRoom(room).stream().map(MessageModel::toModel).collect(Collectors.toList());
    }

    public PageResponse<MessageModel> getAllByRoom(String room, PageRequest pageRequest) {
        QueryResultPage<MessageEntity> scanResultPage = messageRepository.getAllByRoom(room, pageRequest.getSize(),
                pageRequest.getLastKey());
        return buildPageResponse(scanResultPage);
    }

    private PageResponse<MessageModel> buildPageResponse(QueryResultPage<MessageEntity> scanResultPage) {
        return PageResponse.<MessageModel>builder()
                .contents(scanResultPage.getResults().stream().map(MessageModel::toModel).collect(Collectors.toList()))
                .lastKey(Objects.nonNull(scanResultPage.getLastEvaluatedKey())
                        ? MessageCompositeKey.toMessageCompositeKey(scanResultPage.getLastEvaluatedKey())
                        : null)
                .build();
    }

    public void deleteAllByRoom(String room) {
        messageRepository.deleteAllByRoom(room);
    }

}
