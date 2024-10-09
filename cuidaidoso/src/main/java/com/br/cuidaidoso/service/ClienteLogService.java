package com.br.cuidaidoso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.model.ClienteLog;
import com.br.cuidaidoso.repository.ClienteLogRepository;

import java.time.LocalDateTime;

@Service
public class ClienteLogService {

    @Autowired
    private ClienteLogRepository clienteLogRepository;

    public void registrarAcao(Cliente cliente, String acao) {
        ClienteLog log = new ClienteLog();
        log.setAcao(acao);
        log.setHoraAcao(LocalDateTime.now());
        log.setCliente(cliente);

        clienteLogRepository.save(log);
    }
}