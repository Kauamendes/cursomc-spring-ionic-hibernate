package com.kauamendes.projetocurso.domain;

import javax.persistence.Entity;

import com.kauamendes.projetocurso.domain.enums.EstadoPagamento;

@Entity
public class PagamentoComCartão extends Pagamento {

	private static final long serialVersionUID = 1L;
	private Integer numeroDeParcelas;
	
	public PagamentoComCartão() {
		
	}

	public PagamentoComCartão(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
		super(id, estado, pedido);
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}
	
	
	
}
