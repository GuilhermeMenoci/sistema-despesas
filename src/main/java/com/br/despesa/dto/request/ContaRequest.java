package com.br.despesa.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ContaRequest(
		
		@NotBlank(message = "Nome da conta é um campo obrigatório")
		String nomeConta,
		
		@NotBlank(message = "Número da conta é um campo obrigatório")
		String numeroConta,
		
		@NotBlank(message = "Usuário é um campo obrigatório")
		String usuario,
		
		@NotBlank(message = "Senha é um campo obrigatório")
		String senha,
		
		@NotBlank(message = "E-mail é um campo obrigatório")
		String email
		
		) {

}
