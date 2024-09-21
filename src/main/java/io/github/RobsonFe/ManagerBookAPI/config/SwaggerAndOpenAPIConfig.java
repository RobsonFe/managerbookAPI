package io.github.RobsonFe.ManagerBookAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerAndOpenAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Manager Books API")
                                .description("The Manager Book API is a project developed by me using Spring Boot, Spring JPA, Swagger, Docker, and MySQL. This API serves as a management system for books, allowing users to perform CRUD operations on authors and books.")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("Robson Ferreira").email("robsonfe.dev@gmail.com"))
                );
    }
}
