package com.br.cuidaidoso.repository;

import com.br.cuidaidoso.model.CuidadorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuidadorLogRepository extends JpaRepository<CuidadorLog, Long> {
}