package com.example.yeobee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YeobeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeobeeApplication.class, args);
    }
}
