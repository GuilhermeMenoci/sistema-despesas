package com.br.despesa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Generated;

@Generated
@Configuration
public class SwaggerConfig {

	// http://localhost:8080/swagger-ui/index.html
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Sistema de Despesas API")
						.version("1.0")
						.description("API para o sistema de gerenciamento de despesas"));
	}

}
