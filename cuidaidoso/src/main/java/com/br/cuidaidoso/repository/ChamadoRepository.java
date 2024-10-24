package com.br.cuidaidoso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.cuidaidoso.model.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    
}
