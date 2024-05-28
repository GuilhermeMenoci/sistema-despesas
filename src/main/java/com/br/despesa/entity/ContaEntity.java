package com.br.despesa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "conta")
public class ContaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ident")
	private Long id;
	
	@Column(name = "saldo")
	private BigDecimal saldo;
	
	@OneToMany(mappedBy = "conta")
	private List<MovimentacaoContaEntity> movimentacoes;
	
}
