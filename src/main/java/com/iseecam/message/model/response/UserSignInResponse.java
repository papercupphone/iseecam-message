package com.iseecam.message.model.response;

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
public class UserSignInResponse {

    private String message;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String idToken;

}
