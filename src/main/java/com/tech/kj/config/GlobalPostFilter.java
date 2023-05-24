package com.tech.kj.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalPostFilter implements GlobalFilter, Ordered {
	Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	
    	ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        log.info("<<<<<<<<?<<<<<<<<<<<<<",request.getPath().contextPath().toString());
        // Perform any pre-processing tasks on the request

        // Example: Check if the request should be blocked and return a response
        if (shouldBlockRequest(request)) {
            return response.setComplete();
        }

        // Continue with the filter chain
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0; // Set the order of the filter. Lower values have higher priority.
    }

    private boolean shouldBlockRequest(ServerHttpRequest request) {
    	log.info("<<<<<<<<<<<<<<<<?>>>>>>>>>>>>",request.getPath().contextPath().toString());
    	
        return false; // Return true if the request should be blocked, false otherwise
    }
}

