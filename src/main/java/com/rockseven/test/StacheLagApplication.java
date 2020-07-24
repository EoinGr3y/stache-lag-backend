package com.rockseven.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rockseven.test")
public class StacheLagApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StacheLagApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
