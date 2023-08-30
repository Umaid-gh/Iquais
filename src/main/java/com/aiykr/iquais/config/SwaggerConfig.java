package com.aiykr.iquais.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    /**
     * Configures and initializes Swagger for API documentation.
     *
     * @return A Docket instance for Swagger API configuration.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aiykr.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * Provides information about the API documentation.
     *
     * @return An ApiInfo instance containing API metadata.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Iquais Application API Documentation")
                .description("Updated API documentation for Iquais application")
                .version("1.0.0")
                .contact(new Contact("Umaid", "", "mailtoumaid@gmail.com"))
                .build();
    }
}
