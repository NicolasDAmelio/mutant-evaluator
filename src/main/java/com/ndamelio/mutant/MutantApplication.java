package com.ndamelio.mutant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class MutantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutantApplication.class, args);
    }

}