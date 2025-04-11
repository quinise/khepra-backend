package com.khepraptah.khepra_site_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan("com.khepraptah")
public class KhepraSiteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhepraSiteBackendApplication.class, args);
	}
}
