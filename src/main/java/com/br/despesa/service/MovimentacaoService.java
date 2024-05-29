package com.br.despesa.service;

import org.springframework.stereotype.Service;

import com.br.despesa.dto.request.MovimentacaoRequest;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.entity.MovimentacaoContaEntity;
import com.br.despesa.repository.ContaRepository;
import com.br.despesa.repository.MovimentacaoContaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovimentacaoService {

	private final ContaRepository contaRepository;

	private final MovimentacaoContaRepository movimentacaoContaRepository;

	@Transactional
	public void cadastrarMovimentacoes(MovimentacaoRequest movimentacaoRequest) {

		ContaEntity conta = buscarConta(movimentacaoRequest.idConta());

		MovimentacaoContaEntity movimentacao = extrairMovimentacao(movimentacaoRequest, conta);
		movimentacaoContaRepository.save(movimentacao);


	}

	private MovimentacaoContaEntity extrairMovimentacao(MovimentacaoRequest movimentacaoRequest, ContaEntity conta) {
		return MovimentacaoContaEntity.builder()
				.conta(conta)
				.descricao(movimentacaoRequest.descricao())
				.tipoDespesa(movimentacaoRequest.tipoDespesa())
				.tipoMovimentacao(movimentacaoRequest.tipoMovimentacao())
				.build();
	}

	private ContaEntity buscarConta(Long idConta) {
		return contaRepository.findById(idConta).orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada"));
	}

}
