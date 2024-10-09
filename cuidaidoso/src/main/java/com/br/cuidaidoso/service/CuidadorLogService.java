package com.br.cuidaidoso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.model.CuidadorLog;
import com.br.cuidaidoso.repository.CuidadorLogRepository;

import java.time.LocalDateTime;

@Service
public class CuidadorLogService {

    @Autowired
    private CuidadorLogRepository cuidadorLogRepository;

    public void registrarAcao(Cuidador cuidador, String acao) {
        CuidadorLog log = new CuidadorLog();
        log.setAcao(acao);
        log.setHoraAcao(LocalDateTime.now());
        log.setCuidador(cuidador);

        cuidadorLogRepository.save(log);
    }
}