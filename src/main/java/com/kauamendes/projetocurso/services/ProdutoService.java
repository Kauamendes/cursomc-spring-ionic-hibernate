package com.kauamendes.projetocurso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kauamendes.projetocurso.domain.Categoria;
import com.kauamendes.projetocurso.domain.Pedido;
import com.kauamendes.projetocurso.domain.Produto;
import com.kauamendes.projetocurso.repositories.CategoriaRepository;
import com.kauamendes.projetocurso.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository catRepo;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new com.kauamendes.projetocurso.services.exceptions.ObjectNotFoundException(
				"Objeto não encontrado! ID: " +id +", Tipo:" + Pedido.class.getName()));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = catRepo.findAllById(ids);
		
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
		
	}
	
}
