package com.br.despesa.service;

import org.springframework.stereotype.Service;

import com.br.despesa.dto.ContaRequest;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.repository.ContaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContaService {
	
	private final ContaRepository contaRepository;
	
	@Transactional
	public void cadastrarConta(ContaRequest contaRequest) {
		
		ContaEntity conta = ContaEntity.builder()
		.nomeConta(contaRequest.nomeConta())
		.numeroConta(contaRequest.numeroConta())
		.build();
		
		contaRepository.save(conta);
		
	}
	
	private ContaEntity buscarConta(Long idConta) {
		return contaRepository.findById(idConta).orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada"));
	}

}
