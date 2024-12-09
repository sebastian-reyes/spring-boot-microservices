package com.demo.gateway.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.Config> {

    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);

    public SampleCookieGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config cookie) {
        return (exchange, chain) -> {
            logger.info("ejecuntando SampleCookieGatewayFilterFactory: {}", cookie.getMessage());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                exchange.getResponse()
                        .getCookies()
                        .add("color", ResponseCookie.from(cookie.getName(), cookie.getValue()).build());
            }));
        };
    }

    public static class Config {
        private String name;
        private String value;
        private String message;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
