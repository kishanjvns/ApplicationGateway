package com.tech.kj.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalPreFilter implements GlobalFilter, Ordered {
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		log.info("<<<<<<<<?<<<<<<<<<<<<<" + request.getPath().contextPath().toString());
		// Perform any pre-processing tasks on the request

		// Example: Check if the request should be blocked and return a response
		String message = shouldBlockRequest(request);
		if (!StringUtils.isEmpty(message)) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.getHeaders().add("Content-Type", "application/json");
			String errorBody = "{\"error\": \"" + message + "\",";
			errorBody += "\n" + "\"code\": \""+HttpStatus.UNAUTHORIZED +"\"}";
			return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
		}

		// Continue with the filter chain
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1; // Set the order of the filter. Lower values have higher priority.
	}

	private String shouldBlockRequest(ServerHttpRequest request) {
		log.info("<<<<<<<<<<<<<<<<?>>>>>>>>>>>>" + request.getPath().toString());
		String requestedUri = request.getPath().toString();
		if(requestedUri.startsWith("/v1/api/userservice/login")) {
			return "";
		}
		if (requestedUri.contains("/api/")) {
			if (!request.getHeaders().containsKey(("Authorization"))) {
				log.info("Authorization header is missing");
				return "Authorization header is missing";
			}
			String authHeaderValue = request.getHeaders().getFirst("Authorization");
			if (StringUtils.isEmpty(authHeaderValue) || !authHeaderValue.startsWith("Bearer")) {
				log.info("Authorization header has not pressent Bearer token");
				return "Authorization header has not pressent Bearer token";
			}
			//will write else block to check the token has expired or not before forwarding request to the original microservice
		}
		return "";
	}
}
