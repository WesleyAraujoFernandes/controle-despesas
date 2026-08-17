package com.financas.controledespesas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.financas.controledespesas.ControleDespesasApplication;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final ControleDespesasApplication controleDespesasApplication;

    CorsConfig(ControleDespesasApplication controleDespesasApplication) {
        this.controleDespesasApplication = controleDespesasApplication;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
    }
}
