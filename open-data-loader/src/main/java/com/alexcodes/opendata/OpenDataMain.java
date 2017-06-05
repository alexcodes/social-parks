package com.alexcodes.opendata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.alexcodes.*")
public class OpenDataMain {
    public static void main(String[] args) {
        SpringApplication.run(OpenDataMain.class, args);
    }
}
