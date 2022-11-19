package com.iseecam.message.model.request;

import java.util.Objects;
import java.util.function.Consumer;

import com.iseecam.message.exception.ValidationException;

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
public class UserGetRefreshToken {

    private String refreshToken;

    public void validate() {
        validateRefreshToken().accept(this);
    }

    private Consumer<UserGetRefreshToken> validateRefreshToken() {
        return request -> {
            if (Objects.isNull(request.getRefreshToken()) || request.getRefreshToken().isEmpty()) {
                throw new ValidationException("auth.token_refresh_required");
            }
        };
    }

}
