package com.br.despesa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movimentacao_conta")
@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class MovimentacaoContaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ident")
	private Long id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "valor_movimentado")
	private BigDecimal valorMovimentado;
	
	@Column(name = "saldo_atual_conta")
	private BigDecimal saldoAtual;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_movimentacao")
	private TipoMovimentacaoEnum tipoMovimentacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_despesa")
	private TipoDespesaEnum tipoDespesa;
	
	@ManyToOne
	@JoinColumn(name = "id_conta")
	private ContaEntity conta;
	
	@Column(name = "data_movimentacao")
	private ZonedDateTime dataMovimentacao;
	
	@PrePersist
	public void onCreate() {
		dataMovimentacao = ZonedDateTime.now();
	}
	
}
