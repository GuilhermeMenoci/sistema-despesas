package com.br.despesa.factory.dto;

import java.util.List;

import com.br.despesa.dto.response.MovimentacoesContaResponse;
import com.br.despesa.entity.MovimentacaoContaEntity;

public class MovimentacaoResponseFactory {

	private MovimentacaoResponseFactory() {}
	
	public static MovimentacoesContaResponse converterParaReponse(MovimentacaoContaEntity mov) {
		return MovimentacoesContaResponse.builder()
				.id(mov.getId())
				.descricao(mov.getDescricao())
				.valorMovimentado(mov.getValorMovimentado())
				.saldoRestante(mov.getSaldoAtual())
				.tipoMovimentacao(mov.getTipoMovimentacao())
				.tipoDespesa(mov.getTipoDespesa())
				.dataMovimentacao(mov.getDataMovimentacao())
				.build();
	}
	
	public static List<MovimentacoesContaResponse> converterParaListaReponse(List<MovimentacaoContaEntity> movimentacoes){
		return movimentacoes.stream().map(MovimentacaoResponseFactory::converterParaReponse).toList();
	}
	
}
