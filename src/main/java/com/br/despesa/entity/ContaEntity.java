package com.br.despesa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conta")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ContaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ident")
	private Long id;
	
	@Column(name = "nome_conta")
	private String nomeConta;
	
	@Column(name = "numero_conta")
	private String numeroConta;
	
	@Column(name = "saldo")
	private BigDecimal saldo;
	
	@OneToMany(mappedBy = "conta")
	private List<MovimentacaoContaEntity> movimentacoes;
	
	@Column(name = "data_criacao")
	private ZonedDateTime dataCriacao;
	
	@Column(name = "data_atualizacao")
	private ZonedDateTime dataAtualizacao;
	
	@OneToOne
	private UsuarioEntity usuario;
	
	@PrePersist
	public void onCreate() {
		dataCriacao = ZonedDateTime.now();
		saldo = BigDecimal.ZERO;
		dataAtualizacao = ZonedDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate() {
		dataAtualizacao = ZonedDateTime.now();
	}
	
}
