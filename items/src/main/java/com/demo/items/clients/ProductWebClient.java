package com.demo.items.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProductWebClient {

    @Bean
    @LoadBalanced
    WebClient.Builder webClient(){
        return WebClient.builder().baseUrl("http://products");
    }
}
