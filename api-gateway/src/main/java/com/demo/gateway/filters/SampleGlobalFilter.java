package com.demo.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("PRE Request");

        exchange.getRequest()
                .mutate()
                .headers(headers -> headers.add("token", "token-example"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("POST Response");

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token"))
                    .ifPresent(token -> {
                        logger.info("token: {}", token);
                        exchange.getResponse().getHeaders().add("token", token);
                    });

            exchange.getResponse()
                    .getCookies()
                    .add("color", ResponseCookie.from("color", "red").build());

            exchange.getResponse()
                    .getHeaders()
                    .setContentType(MediaType.APPLICATION_JSON);
        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
