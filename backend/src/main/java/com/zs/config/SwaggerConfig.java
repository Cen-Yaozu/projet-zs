package com.zs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("项目接口文档")
                        .description("RESTful API接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ZS")
                                .email("admin@example.com")));
    }
}