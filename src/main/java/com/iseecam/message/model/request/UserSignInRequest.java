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
public class UserSignInRequest {

    private String username;
    private String password;

    public void validate() {
        validateUsername().andThen(validatePassword()).accept(this);
    }

    private Consumer<UserSignInRequest> validateUsername() {
        return request -> {
            if (Objects.isNull(request.getUsername()) || request.getUsername().isEmpty()) {
                throw new ValidationException("auth.username_required");
            }
        };
    }

    private Consumer<UserSignInRequest> validatePassword() {
        return request -> {
            if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()) {
                throw new ValidationException("auth.password_required");
            }
        };
    }
}
