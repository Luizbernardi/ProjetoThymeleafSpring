package com.br.cuidaidoso.repository;

import com.br.cuidaidoso.model.ClienteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteLogRepository extends JpaRepository<ClienteLog, Long> {

}