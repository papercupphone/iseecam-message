package com.iseecam.message.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.iseecam.message.exception.AuthorizationException;
import com.iseecam.message.exception.InternalException;
import com.iseecam.message.exception.ValidationException;
import com.iseecam.message.model.response.ErrorResponse;

@ControllerAdvice
public class RuntimeExceptionHandler {

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        ValidationException ex, WebRequest request) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(ErrorResponse.builder()
                                .message(ex.getMessage())
                                .code("400")
                                .build(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthorizationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        AuthorizationException ex, WebRequest request) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(ErrorResponse.builder()
                                .message(ex.getMessage())
                                .code("401")
                                .build(), HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(InternalException.class)
        public ResponseEntity<ErrorResponse> handleInternalException(
                        InternalException ex, WebRequest request) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(ErrorResponse.builder()
                                .message(ex.getMessage())
                                .code("500")
                                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
