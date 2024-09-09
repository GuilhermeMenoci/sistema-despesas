package com.br.despesa.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.despesa.entity.MovimentacaoContaEntity;

public interface MovimentacaoContaRepository extends JpaRepository<MovimentacaoContaEntity, Long>{

	@Query("select movimentacoes "
		 + "from MovimentacaoContaEntity movimentacoes "
		 + "where movimentacoes.dataMovimentacao between :dataInicio and :dataFim")
	List<MovimentacaoContaEntity> findExtratoMovimentacoes(ZonedDateTime dataInicio, ZonedDateTime dataFim);
	
}
