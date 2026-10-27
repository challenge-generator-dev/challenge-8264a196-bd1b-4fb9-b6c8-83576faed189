package com.fintech.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
public class BackendApplication {
    private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

    private final ApplicationProperties applicationProperties;

    public BackendApplication(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        validateProperties();
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Aplicación iniciada con configuración: ambiente={}, versión={}",
            applicationProperties.getEnvironment(),
            applicationProperties.getVersion());
        logger.info("Configuración de base de datos: url={}", applicationProperties.getDatasource().getUrl());
    }

    private void validateProperties() {
        if (applicationProperties.getEnvironment() == null || applicationProperties.getEnvironment().isBlank()) {
            throw new IllegalStateException("La propiedad 'environment' no puede estar vacía");
        }
        if (applicationProperties.getDatasource().getUrl() == null || applicationProperties.getDatasource().getUrl().isBlank()) {
            throw new IllegalStateException("La URL de la base de datos no puede estar vacía");
        }
        if (applicationProperties.getDatasource().getUsername() == null || applicationProperties.getDatasource().getUsername().isBlank()) {
            throw new IllegalStateException("El nombre de usuario de la base de datos no puede estar vacío");
        }
    }

    public record ApplicationProperties(String environment, String version, DatasourceProperties datasource) {
        public record DatasourceProperties(String url, String username, String password, String driverClassName) {}
    }
}