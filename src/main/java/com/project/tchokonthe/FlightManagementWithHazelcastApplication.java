package com.project.tchokonthe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlightManagementWithHazelcastApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightManagementWithHazelcastApplication.class, args);
    }

}

