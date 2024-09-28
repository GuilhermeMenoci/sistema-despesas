package com.br.despesa.factory.entity;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.entity.UsuarioEntity;

public class ContaEntityFactory {

	private ContaEntityFactory() {}
	
	public static ContaEntity converterParaEntity(ContaRequest contaRequest, UsuarioEntity usuario) {
		return ContaEntity.builder()
		.nomeConta(contaRequest.nomeConta())
		.numeroConta(contaRequest.numeroConta())
		.usuario(usuario)
		.build();
	}
	
}
