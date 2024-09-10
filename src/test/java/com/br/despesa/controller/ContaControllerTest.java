package com.br.despesa.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.dto.response.ContaResponse;
import com.br.despesa.service.ContaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

	@InjectMocks
	private ContaController contaController;
	
	@Mock
	private ContaService contaService;
	
	private MockMvc mockMvc;
	
	private String url;
	
	private ContaRequest contaRequest;
	
	private ContaResponse contaResponse;

	private Long contaId;
	
	private String json;
	
	@Test
	@Order(1)
	void esperaCadastrarUmaConta() throws Exception {
		mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json)
				).andExpect(status().isOk());
		
		verify(contaService).cadastrarConta(contaRequest);
	}
	
	@Test
	@Order(2)
	void esperaBuscarContaPorId() throws Exception {
		when(contaService.buscarContaPorId(anyLong())).thenReturn(contaResponse);

		mockMvc.perform(get("/contas/" + contaId)
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id").value(contaResponse.id()))
	            .andExpect(jsonPath("$.nomeConta").value(contaResponse.nomeConta()))
	            .andExpect(jsonPath("$.numeroConta").value(contaResponse.numeroConta()))
	            .andExpect(jsonPath("$.saldo").value(contaResponse.saldo()));
	}
	
	@BeforeEach
	void setup() throws JsonProcessingException {
		mockMvc = MockMvcBuilders.standaloneSetup(contaController).alwaysDo(print()).build();
		url = "/contas";
		contaRequest = construirContaRequest();
		contaId = 1L;
        contaResponse = construirContaResponse();
		json = new ObjectMapper().writeValueAsString(contaRequest);
	}
	
	private ContaRequest construirContaRequest() {
		return new ContaRequest("Guilherme", "12345");
	}
	
	private ContaResponse construirContaResponse() {
        return ContaResponse.builder()
                .id(1L)
                .nomeConta("Guilherme")
                .numeroConta("12345")
                .saldo(BigDecimal.valueOf(1000.00))
                .dataCriacao(ZonedDateTime.now())
                .build();
    }
	
}
