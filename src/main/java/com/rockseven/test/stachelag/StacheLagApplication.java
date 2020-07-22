package com.rockseven.test.stachelag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.data.CustomerRepository;
import com.rockseven.test.data.RaceRepository;
import com.rockseven.test.data.model.Customer;

import com.rockseven.test.data.model.RaceData;
import com.rockseven.test.data.model.TeamsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rockseven.test.data")
public class StacheLagApplication implements CommandLineRunner {

    @Autowired
    private RaceRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(StacheLagApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//		customerRepository.deleteAll();
//
//		customerRepository.save(new Customer("Alice", "Smith"));
//		customerRepository.save(new Customer("Bob", "Smith"));
//
//		// fetch all customers
//		System.out.println("Customers found with findAll():");
//		System.out.println("-------------------------------");
//		for (Customer customer : customerRepository.findAll()) {
//		System.out.println(customer);
//		}
//		System.out.println();
//
//		// fetch an individual customer
//		System.out.println("Customer found with findByFirstName('Alice'):");
//		System.out.println("--------------------------------");
//		System.out.println(customerRepository.findByFirstName("Alice"));
//
//		System.out.println("Customers found with findByLastName('Smith'):");
//		System.out.println("--------------------------------");
//		for (Customer customer : customerRepository.findByLastName("Smith")) {
//		System.out.println(customer);
        ObjectMapper objectMapper = new ObjectMapper();
        repository.deleteAll();

        RaceData raceData = objectMapper.readValue(new File("src/main/resources/inputData/positions-edit.json"), RaceData.class);
        repository.save(raceData);

        for (RaceData raceDataEntry : repository.findAll()) {
            System.out.println("Race Data Entry:");
            System.out.println(raceData);
        }
    }
}
