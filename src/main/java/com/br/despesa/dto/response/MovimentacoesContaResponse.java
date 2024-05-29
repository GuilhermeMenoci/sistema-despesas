package com.br.despesa.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;

import lombok.Builder;

@Builder
public record MovimentacoesContaResponse(
		
		Long id,
		String descricao,
		BigDecimal valorMovimentado,
		BigDecimal saldoRestante,
		TipoMovimentacaoEnum tipoMovimentacao,
		TipoDespesaEnum tipoDespesa,
		ZonedDateTime dataMovimentacao
		
		) {

}
