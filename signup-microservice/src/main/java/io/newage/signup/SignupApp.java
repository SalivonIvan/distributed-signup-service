package io.newage.signup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
//@ComponentScan(basePackages = "io.newage.signup")
public class SignupApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SignupApp.class, args);
    }
}
