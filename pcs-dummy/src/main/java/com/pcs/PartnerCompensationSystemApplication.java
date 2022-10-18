package com.pcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class PartnerCompensationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerCompensationSystemApplication.class, args);
	}

}
