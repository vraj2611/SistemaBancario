package edu.iff.sistemabanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//spring.datasource.url=jdbc:mysql://root:@localhost:3306/sistemabancario
@SpringBootApplication
public class SistemaBancarioApplication {
	public static void main(String[] args) {
		SpringApplication.run(SistemaBancarioApplication.class, args);
	}
}
