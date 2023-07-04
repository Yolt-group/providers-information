package com.yolt.pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProvidersInformationApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProvidersInformationApplication.class);
        application.run(args);
    }
}
