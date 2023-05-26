package com.tech.kj.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfiguration {

	@Bean
	public RouteLocator routesUserService(RouteLocatorBuilder builder) {
		return builder.routes().route("userModule", r-> r.path("/v1/api/userservice/**").uri("lb://User-Service")).build();
	}
	@Bean
	public RouteLocator routesTeacherService(RouteLocatorBuilder builder) {
		return null;
	}
}
