package com.yeogiya.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = {"com.yeogiya"})
@EnableJpaRepositories(basePackages = {"com.yeogiya"})
public class YeogiyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeogiyaApplication.class, args);
	}

}
