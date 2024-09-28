package com.br.despesa.factory.dto;

import com.br.despesa.dto.response.ContaResponse;
import com.br.despesa.entity.ContaEntity;

public class ContaResponseFactory {

	private ContaResponseFactory() {}
	
	public static ContaResponse converterReponse(ContaEntity conta) {
		return ContaResponse.builder()
				.id(conta.getId())
				.nomeConta(conta.getNomeConta())
				.numeroConta(conta.getNumeroConta())
				.saldo(conta.getSaldo())
				.dataCriacao(conta.getDataCriacao())
				.build();
	}
	
}
