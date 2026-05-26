package com.demo.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.demo.users")
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}
