package com.br.despesa.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "movimentacao_conta")
@Builder
public class MovimentacaoContaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ident")
	private Long id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_movimentacao")
	private TipoMovimentacaoEnum tipoMovimentacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_despesa")
	private TipoDespesaEnum tipoDespesa;
	
	@ManyToOne
	@JoinColumn(name = "id")
	private ContaEntity conta;
	
	@Column(name = "data_criacao")
	private ZonedDateTime dataCriacao;
	
	@PrePersist
	public void onCreate() {
		dataCriacao = ZonedDateTime.now();
	}
	
}
