package org.gcdms.gcdmssaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.gcdms.gcdmssaas")
public class GcdmsSaasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcdmsSaasApplication.class, args);
	}

}
