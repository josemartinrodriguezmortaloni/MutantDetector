package com.example.MutantDetector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mutantDetectorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .description("API REST para detectar mutantes analizando secuencias de ADN. Examen técnico de MercadoLibre.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu.email@ejemplo.com")
                                .url("https://github.com/tu-usuario"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}