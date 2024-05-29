package com.br.despesa.dto;

import java.math.BigDecimal;

import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;

public record MovimentacaoRequest(
		
		Long idConta,
		BigDecimal quantidadeMovimentar,
		String descricao,
		TipoMovimentacaoEnum tipoMovimentacao,
		TipoDespesaEnum tipoDespesa
		
		) {

}
