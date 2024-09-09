package com.br.despesa.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record FiltroRequstExtrato(
		
		@NotNull(message = "Data de início é obrigatório")
		LocalDate dataInicio, 
		
		LocalDate dataFim
		
		) {

}
