package com.br.despesa.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TipoDespesaEnum {

	TP_EDUCACAO("Educação"),
	TP_INVESTIMENTO("Investimento"),
	TP_LAZER("Lazer"),
	TP_VIAGEM("Viagem"),
	TP_ANIMAIS("Animais"),
	TP_TRANSPORTE("Transporte"),
	TP_ALIMENTACAO("Alimentação"),
	TP_SAUDE("Saúde"),
	TP_SEGUROS("Seguros"),
	TP_OUTROS("Outros");
	
	private String descricao;
	
}
