package br.com.fiap.locatech.locatech.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI locaTech(){
        return new OpenAPI().info(new Info().title("Loca Tech API")
                .description("API de locação de veículos. As rotas legadas, /v1 e /v2 coexistem.")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("https://github.com/pedropaulofarias"))
        );
    }
}
