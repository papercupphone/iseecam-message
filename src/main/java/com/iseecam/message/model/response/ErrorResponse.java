package com.iseecam.message.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ErrorResponse {
    
    private String message;
    private String code;
    
}
