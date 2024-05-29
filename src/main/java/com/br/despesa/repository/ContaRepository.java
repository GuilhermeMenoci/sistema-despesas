package com.br.despesa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.despesa.entity.ContaEntity;

public interface ContaRepository extends JpaRepository<ContaEntity, Long>{

	Optional<ContaEntity> findByNumeroConta(String numeroConta);
	
}
