package com.kauamendes.projetocurso.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.kauamendes.projetocurso.services.DBService;
import com.kauamendes.projetocurso.services.EmailService;
import com.kauamendes.projetocurso.services.MockEmailService;

public class TestConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}

	@Bean
	public EmailService emailService() {
		return new MockEmailService() {
		};
	}
	
}
