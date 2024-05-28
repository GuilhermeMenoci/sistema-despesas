package com.br.despesa.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TipoMovimentacaoEnum {

	MOV_ENTRADA("Entrada"),
	MOV_SAIDA("Saída");

	private String descricao;
	
}
