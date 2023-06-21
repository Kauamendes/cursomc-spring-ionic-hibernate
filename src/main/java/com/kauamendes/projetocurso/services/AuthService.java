package com.kauamendes.projetocurso.services;

import java.util.Iterator;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kauamendes.projetocurso.domain.Cliente;
import com.kauamendes.projetocurso.repositories.ClienteRepository;
import com.kauamendes.projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("Cliente"));
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	//Usado tabela unicode para gerar numeros e digitos aleatorios atraves do codigo
	private char randomChar() {
		int opt = rand.nextInt(3);
		if (opt == 0) { //gera digito
			return (char) (rand.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		} else { //gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}
}
