package com.br.despesa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.despesa.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

	boolean existsByUsuario(String usuario);
	
}
