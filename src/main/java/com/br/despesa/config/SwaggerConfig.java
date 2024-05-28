package com.br.despesa.config;


import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Generated;

@Generated
@Configuration
public class SwaggerConfig {

	// http://localhost:8080/swagger-ui/index.html
	@Bean
	GroupedOpenApi swagger() {
		return GroupedOpenApi.builder()
				.group("com.br.despesa")
				.packagesToScan("com.br.despesa")
				.build();
	}
	
}
