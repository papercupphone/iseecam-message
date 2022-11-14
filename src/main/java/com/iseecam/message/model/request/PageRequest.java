package com.iseecam.message.model.request;

import com.iseecam.message.key.MessageCompositeKey;

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
public class PageRequest {

    private MessageCompositeKey compositeKey;
    private int size;

}
