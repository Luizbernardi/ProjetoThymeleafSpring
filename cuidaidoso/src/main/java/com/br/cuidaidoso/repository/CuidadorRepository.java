package com.br.cuidaidoso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.cuidaidoso.model.Cuidador;

@Repository
public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {

}
