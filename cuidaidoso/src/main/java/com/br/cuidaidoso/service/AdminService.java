package com.br.cuidaidoso.service;

import com.br.cuidaidoso.model.Admin;
import com.br.cuidaidoso.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Admin admin) {
        admin.setSenha(passwordEncoder.encode(admin.getSenha()));
        adminRepository.save(admin);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}