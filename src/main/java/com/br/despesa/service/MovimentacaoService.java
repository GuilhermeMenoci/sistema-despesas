package com.br.despesa.service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.br.despesa.dto.request.FiltroRequstExtrato;
import com.br.despesa.dto.request.MovimentacaoRequest;
import com.br.despesa.dto.response.MovimentacaoResponse;
import com.br.despesa.dto.response.MovimentacoesContaResponse;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.entity.MovimentacaoContaEntity;
import com.br.despesa.enuns.TipoDespesaEnum;
import com.br.despesa.enuns.TipoMovimentacaoEnum;
import com.br.despesa.factory.dto.ContaResponseFactory;
import com.br.despesa.factory.dto.MovimentacaoResponseFactory;
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

		ContaEntity conta = buscarContaPorNumero(movimentacaoRequest.numeroConta());
		atualizarSaldoConta(movimentacaoRequest, conta);
		
		MovimentacaoContaEntity movimentacao = extrairMovimentacao(movimentacaoRequest, conta);
		movimentacaoContaRepository.save(movimentacao);

	}

	private void atualizarSaldoConta(MovimentacaoRequest movimentacaoRequest, ContaEntity conta) {
		BigDecimal saldoNovo;
		
		if(Objects.equals(TipoMovimentacaoEnum.MOV_ENTRADA, movimentacaoRequest.tipoMovimentacao())) {
			saldoNovo =  conta.getSaldo().add(movimentacaoRequest.quantidadeMovimentar());
			
		} else {
			saldoNovo =  conta.getSaldo().subtract(movimentacaoRequest.quantidadeMovimentar());
		}
		
		if(saldoNovo.compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Seu saldo ficará negativo");
		}
		
		conta.setSaldo(saldoNovo);
		contaRepository.save(conta);
	}

	private MovimentacaoContaEntity extrairMovimentacao(MovimentacaoRequest movimentacaoRequest, ContaEntity conta) {
		return MovimentacaoContaEntity.builder()
				.conta(conta)
				.descricao(movimentacaoRequest.descricao())
				.tipoDespesa(movimentacaoRequest.tipoDespesa())
				.tipoMovimentacao(movimentacaoRequest.tipoMovimentacao())
				.valorMovimentado(Objects.equals(TipoMovimentacaoEnum.MOV_SAIDA, movimentacaoRequest.tipoMovimentacao()) ? movimentacaoRequest.quantidadeMovimentar().negate() : movimentacaoRequest.quantidadeMovimentar())
				.saldoAtual(conta.getSaldo())
				.build();
	}

	private ContaEntity buscarContaPorNumero(String numeroConta) {
		return contaRepository.findByNumeroConta(numeroConta).orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada"));
	}

	public MovimentacaoResponse buscarMovimentacoesConta(Long idConta) {
		
		ContaEntity conta = buscarConta(idConta);
		
		return MovimentacaoResponse.builder()
		.conta(ContaResponseFactory.converterReponse(conta))
		.movimentacoes(MovimentacaoResponseFactory.converterParaListaReponse(conta.getMovimentacoes()))
		.build();
		
	}
	
	private ContaEntity buscarConta(Long idConta) {
		return contaRepository.findById(idConta).orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada"));
	}

	public List<TipoMovimentacaoEnum> listarTiposMovimentacoes() {
		return Arrays.asList(TipoMovimentacaoEnum.values());
	}

	public List<TipoDespesaEnum> listarTipoDespesas() {
		return Arrays.asList(TipoDespesaEnum.values());
	}

	public List<MovimentacoesContaResponse> pesquisarExtratoMovimentacoes(FiltroRequstExtrato filtros) {
		ZonedDateTime dataInicio = filtros.dataInicio().atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime dataFim = ZonedDateTime.now().plusDays(1).minusNanos(1);
		
		if(Objects.nonNull(filtros.dataFim()))
			dataFim = filtros.dataFim().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);
		
		
		List<MovimentacaoContaEntity> extratos = movimentacaoContaRepository.findExtratoMovimentacoes(dataInicio, dataFim);
		return MovimentacaoResponseFactory.converterParaListaReponse(extratos);
	}

}
