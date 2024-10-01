package com.br.cuidaidoso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.br.cuidaidoso")
public class CuidaidosoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuidaidosoApplication.class, args);
	}

}
