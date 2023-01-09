package com.kauamendes.projetocurso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kauamendes.projetocurso.DTO.ClienteDTO;
import com.kauamendes.projetocurso.DTO.ClienteNewDTO;
import com.kauamendes.projetocurso.domain.Categoria;
import com.kauamendes.projetocurso.domain.Cidade;
import com.kauamendes.projetocurso.domain.Cliente;
import com.kauamendes.projetocurso.domain.Endereco;
import com.kauamendes.projetocurso.domain.enums.TipoCliente;
import com.kauamendes.projetocurso.repositories.ClienteRepository;
import com.kauamendes.projetocurso.repositories.EnderecoRepository;
import com.kauamendes.projetocurso.services.exceptions.DateIntegrityException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	@Autowired
	private BCryptPasswordEncoder be;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new com.kauamendes.projetocurso.services.exceptions.ObjectNotFoundException(
				"Objeto não encontrado! ID: " +id +", Tipo:" + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj = repo.save(obj);
		enderecoRepo.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
		repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DateIntegrityException("Não é possivel excluir porque há pedidos relacionadas");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		 return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		 Cliente cli1 = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), be.encode(objDto.getSenha()));
		 Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		 Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli1, cid);
		 cli1.getEnderecos().add(end);
		 end.setCidade(cid);
		 cli1.getTelefones().add(objDto.getTelefone1());
		 
		 if(objDto.getTelefone2() != null) {
			 cli1.getTelefones().add(objDto.getTelefone2());
		 }
		 
		 if(objDto.getTelefone3() != null) {
			 cli1.getTelefones().add(objDto.getTelefone3());
		 }
		 
		 return cli1;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
