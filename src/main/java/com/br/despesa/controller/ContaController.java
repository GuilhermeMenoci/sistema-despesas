package com.br.despesa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.despesa.dto.MovimentacaoRequest;
import com.br.despesa.service.ContaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contas")
public class ContaController {

	private final ContaService contaService;
	
	 @PostMapping("/movimentacoes")
	 public void cadastrarMovimentacoes(@RequestBody MovimentacaoRequest movimentacao) {
		 contaService.cadastrarMovimentacoes(movimentacao);
	 }
	
}
