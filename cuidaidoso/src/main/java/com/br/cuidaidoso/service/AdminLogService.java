package com.br.cuidaidoso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.cuidaidoso.model.Admin;
import com.br.cuidaidoso.model.AdminLog;
import com.br.cuidaidoso.repository.AdminLogRepository;

import java.time.LocalDateTime;

@Service
public class AdminLogService {

    @Autowired
    private AdminLogRepository adminLogRepository;

    public void registrarAcao(Admin admin, String acao) {
        AdminLog log = new AdminLog();
        log.setAcao(acao);
        log.setHoraAcao(LocalDateTime.now());
        log.setAdmin(admin);

        adminLogRepository.save(log);
    }
}