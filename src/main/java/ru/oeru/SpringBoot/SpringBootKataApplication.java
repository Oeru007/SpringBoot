package ru.oeru.SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringBootKataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKataApplication.class, args);
	}

}
