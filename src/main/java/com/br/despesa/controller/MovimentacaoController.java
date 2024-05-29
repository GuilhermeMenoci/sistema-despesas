package com.br.despesa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.despesa.dto.request.MovimentacaoRequest;
import com.br.despesa.dto.response.MovimentacaoResponse;
import com.br.despesa.service.MovimentacaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

	private final MovimentacaoService movimentacaoService;
	
	@PostMapping()
	public void cadastrarMovimentacoes(@RequestBody MovimentacaoRequest movimentacao) {
		movimentacaoService.cadastrarMovimentacoes(movimentacao);
	}
	
	@GetMapping("/{idConta}/contas")
	 public MovimentacaoResponse buscarMovimentacoesConta(@PathVariable Long idConta) {
		 return movimentacaoService.buscarMovimentacoesConta(idConta);
	 }
	
}
