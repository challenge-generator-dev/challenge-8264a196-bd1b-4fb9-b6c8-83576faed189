package com.fintech.backend.infrastructure.config;


import com.fintech.backend.domain.model.Transaction;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:backend}")
    private String applicationName;

    @Value("${app.version:0.0.1-SNAPSHOT}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinTech Transaction API")
                        .description("API REST para procesamiento de transacciones financieras. " +
                                "Permite crear, consultar y gestionar transacciones bancarias con soporte " +
                                "para múltiples tipos de transacción y estados.")
                        .version(version)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo Fintech")
                                .email("dev@fintech.com")
                                .url("https://fintech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}