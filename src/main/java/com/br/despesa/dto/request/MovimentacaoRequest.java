package com.br.despesa.dto.request;

import java.math.BigDecimal;

import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;

public record MovimentacaoRequest(
		
		String numeroConta,
		BigDecimal quantidadeMovimentar,
		String descricao,
		TipoMovimentacaoEnum tipoMovimentacao,
		TipoDespesaEnum tipoDespesa
		
		) {

}
