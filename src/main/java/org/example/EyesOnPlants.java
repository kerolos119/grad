package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.repo")
@EnableScheduling
public class EyesOnPlants {
    public static void main(String[] args) {
        SpringApplication.run(EyesOnPlants.class,args);
    }

}