package com.br.despesa.factory.entity;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.entity.UsuarioEntity;

public class UsuarioEntityFactory {

	private UsuarioEntityFactory() {}
	
	public static UsuarioEntity converterParaEntity(ContaRequest contaRequest) {
		
		return UsuarioEntity.builder()
		.usuario(contaRequest.usuario())
		.senha(contaRequest.senha())
		.email(contaRequest.email())
		.build();
		
	}
	
}
