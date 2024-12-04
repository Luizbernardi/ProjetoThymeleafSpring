package com.br.cuidaidoso.service;

import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        clienteRepository.save(cliente);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}