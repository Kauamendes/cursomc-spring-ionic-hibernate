package com.kauamendes.projetocurso.services;

import org.springframework.mail.SimpleMailMessage;

import com.kauamendes.projetocurso.domain.Cliente;
import com.kauamendes.projetocurso.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
