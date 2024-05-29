package com.br.despesa.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.dto.response.ContaResponse;
import com.br.despesa.service.ContaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contas")
public class ContaController {

	private final ContaService contaService;
	
	 @PostMapping()
	 public void cadastrarConta(@RequestBody ContaRequest contaRequest) {
		 contaService.cadastrarConta(contaRequest);
	 }
	 
	 @GetMapping("/{id}")
	 public ContaResponse buscarContaPorId(@PathVariable Long id) {
		 return contaService.buscarContaPorId(id);
	 }
	 
	 @GetMapping("/{id}/relatorios/despesas")
	 @ResponseBody
	 public ResponseEntity<Resource> gerarPdfEtiquetaUp(@PathVariable Long id) {
		 HttpHeaders headersOut = new HttpHeaders();
		 headersOut.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		 headersOut.setContentDispositionFormData("attachment", "despesas.xls");

		 return ResponseEntity.ok().headers(headersOut).body(contaService.gerarXlsDespesas(id));
	 }
	
}
