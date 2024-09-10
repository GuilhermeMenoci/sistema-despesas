package com.br.despesa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.repository.ContaRepository;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

	@InjectMocks
	private ContaService contaService;
	
	@Mock
	private ContaRepository contaRepository;
	
	@Test
	@Order(1)
	void esperaCadastrarUmaConta() {
		ContaRequest contaRequest = construirContaRequest();
		
		when(contaRepository.existsByNumeroConta(contaRequest.numeroConta())).thenReturn(false);
		contaService.cadastrarConta(contaRequest);
		verify(contaRepository, times(1)).save(any(ContaEntity.class));
		verifyNoMoreInteractions(contaRepository);
	}
	
	public ContaRequest construirContaRequest() {
		return new ContaRequest("Guilherme", "12345");
	}
	
}
