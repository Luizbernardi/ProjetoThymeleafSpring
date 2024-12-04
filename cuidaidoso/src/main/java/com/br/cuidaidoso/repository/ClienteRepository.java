package com.br.cuidaidoso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.cuidaidoso.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsername(String username);

}
