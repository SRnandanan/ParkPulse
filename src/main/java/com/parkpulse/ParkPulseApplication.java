package com.parkpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkPulseApplication.class, args);
    }
}
