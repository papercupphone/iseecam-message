package com.iseecam.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.iseecam.message.controller.AuthController;
import com.iseecam.message.controller.ConnectController;
import com.iseecam.message.controller.JoinController;
import com.iseecam.message.controller.LeaveController;
import com.iseecam.message.controller.MessageController;
import com.iseecam.message.controller.PingController;

@SpringBootApplication
@Import({ PingController.class, MessageController.class, LeaveController.class, ConnectController.class,
        JoinController.class, AuthController.class })

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}