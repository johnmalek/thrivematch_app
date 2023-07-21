package com.thrivematch.ThriveMatch;

import com.cloudinary.Cloudinary;
import com.thrivematch.ThriveMatch.model.AdminEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableWebSecurity
public class ThriveMatchApplication {

	public static void main(String[] args) {

		SpringApplication.run(ThriveMatchApplication.class, args);
	}

	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary = null;
		Map config = new HashMap();
		String cloudName = "dkkxufuqp";
		String apiKey = "368124135582233";
		String apiSecret = "XS_sjYRR_lYqQUX-m0sIXGEXxbU";
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//						.allowedOriginPatterns("*")
//						.allowedMethods("*")
//						.allowedHeaders("*")
//						.allowCredentials(true);
//			}
//		};
//	}

}
