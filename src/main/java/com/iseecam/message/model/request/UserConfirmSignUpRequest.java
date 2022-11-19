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
public class UserConfirmSignUpRequest {

    private String code;
    private String username;

    public void validate() {
        validateUsername().andThen(validateCode()).accept(this);
    }

    private Consumer<UserConfirmSignUpRequest> validateUsername() {
        return request -> {
            if (Objects.isNull(request.getUsername()) || request.getUsername().isEmpty()) {
                throw new ValidationException("auth.username_required");
            }
        };
    }

    private Consumer<UserConfirmSignUpRequest> validateCode() {
        return request -> {
            if (Objects.isNull(request.getCode()) || request.getCode().isEmpty()) {
                throw new ValidationException("auth.code_required");
            }
        };
    }

}
