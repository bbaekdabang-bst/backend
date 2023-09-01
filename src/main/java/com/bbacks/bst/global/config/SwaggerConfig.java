package com.bbacks.bst.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer");

        return new OpenAPI().components(new Components().addSecuritySchemes("Bearer", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }
}
