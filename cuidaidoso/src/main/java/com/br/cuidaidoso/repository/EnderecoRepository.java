package com.br.cuidaidoso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.cuidaidoso.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Endereco findByCep(String cep);
}
