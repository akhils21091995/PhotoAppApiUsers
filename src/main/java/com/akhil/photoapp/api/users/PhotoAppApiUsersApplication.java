package com.akhil.photoapp.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.akhil.photoapp.api.users.shared.FeignErrorDecoder;

import feign.Logger;

@SpringBootApplication
@EnableFeignClients
public class PhotoAppApiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();

	}

	@Bean
	Logger.Level feignLoggerLover() {
		return Logger.Level.FULL;
	}
	
// If we have declared any class with @Component annotation already,then springboot will perform classpath scan automatically
	//and it is not needed to declare that class with @Bean annotation manually.
//	@Bean
//	public FeignErrorDecoder getFeignErrorDecoder() {
//		return new FeignErrorDecoder();
//	}
}
