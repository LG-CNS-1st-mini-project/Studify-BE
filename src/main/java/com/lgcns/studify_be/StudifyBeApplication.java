package com.lgcns.studify_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StudifyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudifyBeApplication.class, args);
	}

}
