package com.br.despesa.dto.request;

import java.math.BigDecimal;

import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(
		
		@NotBlank(message = "Número da conta é um campo obrigatório")
		String numeroConta,
		
		BigDecimal quantidadeMovimentar,
		String descricao,
		
		@NotNull(message = "Tipo da movimentação é um campo obrigatório")
		TipoMovimentacaoEnum tipoMovimentacao,
		
		@NotNull(message = "Tipo da despesa é um campo obrigatório")
		TipoDespesaEnum tipoDespesa
		
		) {

}
