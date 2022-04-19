package com.ap.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ApConsumerApplication {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApConsumerApplication.class, args);
	}
	@GetMapping("/order/info")
    public String info(@Value("${server.port}") String port) {
        return "Order 서비스의 기본 동작 Port: {" + port + "}";
    }
	/*
	 * @GetMapping("/ap") public void info2(@Value("${server.port}") String port) {
	 * System.out.println("asd"); }
	 */
}
