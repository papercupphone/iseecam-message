package com.iseecam.message.key;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public interface CompositeKey {

    public Map<String, AttributeValue> toAttributeValueMap();

}
