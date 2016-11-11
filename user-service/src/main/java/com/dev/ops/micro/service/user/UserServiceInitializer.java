package com.dev.ops.micro.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dev.ops.micro.service.user"})
public class UserServiceInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceInitializer.class, args);
	}
}