package com.aiykr.iquais;

import com.aiykr.iquais.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Main application class for the Iquais application.
 */
@SpringBootApplication
@Import(SwaggerConfig.class)
public class IquaisApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(IquaisApplication.class, args);
    }
}
