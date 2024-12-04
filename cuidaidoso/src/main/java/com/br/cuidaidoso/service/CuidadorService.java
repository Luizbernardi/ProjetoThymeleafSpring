package com.br.cuidaidoso.service;

import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.repository.CuidadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CuidadorService {

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Cuidador cuidador) {
        cuidador.setSenha(passwordEncoder.encode(cuidador.getSenha()));
        cuidadorRepository.save(cuidador);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}