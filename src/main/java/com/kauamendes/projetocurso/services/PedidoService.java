package com.kauamendes.projetocurso.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kauamendes.projetocurso.domain.Categoria;
import com.kauamendes.projetocurso.domain.Cliente;
import com.kauamendes.projetocurso.domain.ItemPedido;
import com.kauamendes.projetocurso.domain.PagamentoComBoleto;
import com.kauamendes.projetocurso.domain.Pedido;
import com.kauamendes.projetocurso.domain.enums.EstadoPagamento;
import com.kauamendes.projetocurso.repositories.ClienteRepository;
import com.kauamendes.projetocurso.repositories.ItemPedidoRepository;
import com.kauamendes.projetocurso.repositories.PagamentoRepository;
import com.kauamendes.projetocurso.repositories.PedidoRepository;
import com.kauamendes.projetocurso.security.UserSS;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagRepo;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	//@Autowired 
	//private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new com.kauamendes.projetocurso.services.exceptions.ObjectNotFoundException(
				"Objeto não encontrado! ID: " +id +", Tipo:" + Pedido.class.getName()));
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		Cliente cliente = clienteService.find(user.getId()); 
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findByCliente(cliente,pageRequest);
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		pagRepo.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setDesconto(0);
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		//emailService.sendOrderConfirmationEmail(obj);
		return obj;
		
	}
	
}
