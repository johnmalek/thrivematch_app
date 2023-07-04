package com.thrivematch.ThriveMatch;

import com.thrivematch.ThriveMatch.model.AdminEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
public class ThriveMatchApplication {

	public static void main(String[] args) {

		SpringApplication.run(ThriveMatchApplication.class, args);
	}

	@Bean
	AdminEntity setAdminEntity(){
		return new AdminEntity();
	}

}
