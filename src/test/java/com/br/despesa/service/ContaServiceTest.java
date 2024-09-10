package com.br.despesa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.dto.response.ContaResponse;
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
	
	@Test
	@Order(2)
	void esperaNaoCadastrarUmaContaPorJaExistirUma() {
		ContaRequest contaRequest = construirContaRequest();
		
		when(contaRepository.existsByNumeroConta(contaRequest.numeroConta())).thenReturn(true);
        
		assertThrows(RuntimeException.class, () -> contaService.cadastrarConta(contaRequest));
		verify(contaRepository, never()).save(any(ContaEntity.class));
		
	}
	
	@Test
	@Order(3)
    void esperaBuscarContaPorId() {
        Long idConta = 1L;
        ContaEntity contaEntity = construirContaEntity(idConta);

        when(contaRepository.findById(idConta)).thenReturn(Optional.of(contaEntity));
        ContaResponse contaResponse = contaService.buscarContaPorId(idConta);

        assertNotNull(contaResponse);
        assertEquals(idConta, contaResponse.id());
        assertEquals("Guilherme", contaResponse.nomeConta());
        assertEquals("12345", contaResponse.numeroConta());
        assertEquals(BigDecimal.valueOf(1000.00), contaResponse.saldo());
    }
	
	@Test
	@Order(4)
    void esperaNaoEncontrarContaPorId() {
        Long idConta = 1L;

        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            contaService.buscarContaPorId(idConta);
        });

        assertEquals("Nenhuma conta encontrada", thrown.getMessage());
    }

	private ContaEntity construirContaEntity(Long idConta) {
		return ContaEntity.builder()
                .id(idConta)
                .nomeConta("Guilherme")
                .numeroConta("12345")
                .saldo(BigDecimal.valueOf(1000.00))
                .dataCriacao(ZonedDateTime.now())
                .build();
	}
	
	private ContaRequest construirContaRequest() {
		return new ContaRequest("Guilherme", "12345");
	}
	
}
