package com.alexcodes.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.alexcodes.*")
public class TwitterMain {
    public static void main(String[] args) {
        SpringApplication.run(TwitterMain.class, args);
    }
}
