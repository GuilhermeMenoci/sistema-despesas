package com.br.despesa.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record MovimentacaoResponse(
		
		ContaResponse conta,
		List<MovimentacoesContaResponse> movimentacoes
		
		) {

}
