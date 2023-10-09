package com.test.restfulusers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class RestfulUsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulUsersApplication.class, args);
    }
}
