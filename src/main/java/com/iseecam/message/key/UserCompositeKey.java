package com.iseecam.message.key;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

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
@NoArgsConstructor
@AllArgsConstructor
public class UserCompositeKey implements CompositeKey {

    private String username;
    private String room;

    public Map<String, AttributeValue> toAttributeValueMap() {
        Map<String, AttributeValue> messageCompositeKey = new HashMap<>();
        messageCompositeKey.put("id",
                new AttributeValue().withS(username));
        messageCompositeKey.put("room",
                new AttributeValue().withS(room));
        return messageCompositeKey;
    }

    public static MessageCompositeKey toMessageCompositeKey(Map<String, AttributeValue> attributeValueMap) {
        return MessageCompositeKey.builder()
                .id(attributeValueMap.get("username").getS())
                .room(attributeValueMap.get("room").getS())
                .build();
    }

}
