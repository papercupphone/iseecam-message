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
public class UserSignUpRequest {

    private String username;
    private String password;
    private String email;

    public void validate()  {
        validateEmail().andThen(validateUsername()).andThen(validatePassword()).accept(this);
    }

    private Consumer<UserSignUpRequest> validateUsername() {
        return request -> {
            if (Objects.isNull(request.getUsername()) || request.getUsername().isEmpty()) {
                throw new ValidationException("auth.username_required");
            }
        };
    }

    private Consumer<UserSignUpRequest> validatePassword() {
        return request -> {
            if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()) {
                throw new ValidationException("auth.passwordRequired");
            }
        };
    }

    private Consumer<UserSignUpRequest> validateEmail() {
        return request -> {
            if (Objects.isNull(request.getEmail()) || request.getEmail().isEmpty()) {
                throw new ValidationException("auth.email_required");
            }
        };
    }
}
