package com.alexcodes.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.alexcodes.*")
public class WebMain {
    public static void main(String[] args) {
        SpringApplication.run(WebMain.class);
    }
}
