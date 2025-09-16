package com.lgcns.studify_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.lgcns.studify_be")
@EnableJpaRepositories(basePackages = "com.lgcns.studify_be.repository")
@EntityScan(basePackages = "com.lgcns.studify_be.entity")
public class StudifyBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudifyBeApplication.class, args);
    }
}
