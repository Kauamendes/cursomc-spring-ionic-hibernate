package com.kauamendes.projetocurso.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kauamendes.projetocurso.domain.Pedido;
import com.kauamendes.projetocurso.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido buscarPorId(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new com.kauamendes.projetocurso.services.exceptions.ObjectNotFoundException(
				"Objeto não encontrado! ID: " +id +", Tipo:" + Pedido.class.getName()));
	}
	
}
