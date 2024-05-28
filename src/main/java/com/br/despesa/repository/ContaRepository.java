package com.br.despesa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.despesa.entity.ContaEntity;

public interface ContaRepository extends JpaRepository<ContaEntity, Long>{

}
