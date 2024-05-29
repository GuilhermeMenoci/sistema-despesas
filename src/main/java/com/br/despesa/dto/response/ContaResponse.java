package com.br.despesa.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;

@Builder
public record ContaResponse(
		
		Long id,
		String nomeConta,
		String numeroConta,
		BigDecimal saldo,
		ZonedDateTime dataCriacao
		
		) {

}
