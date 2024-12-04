package com.br.cuidaidoso.service;

import com.br.cuidaidoso.model.Admin;
import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.repository.AdminRepository;
import com.br.cuidaidoso.repository.ClienteRepository;
import com.br.cuidaidoso.repository.CuidadorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final CuidadorRepository cuidadorRepository;
    private final ClienteRepository clienteRepository;

    public CustomUserDetailsService(AdminRepository adminRepository, CuidadorRepository cuidadorRepository,
            ClienteRepository clienteRepository) {
        this.adminRepository = adminRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getUsername())
                    .password(admin.getSenha())
                    .roles(admin.getPerfil().name())
                    .build();
        }

        Cuidador cuidador = cuidadorRepository.findByUsername(username);
        if (cuidador != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(cuidador.getUsername())
                    .password(cuidador.getSenha())
                    .roles(cuidador.getPerfil().name())
                    .build();
        }

        Cliente cliente = clienteRepository.findByUsername(username);
        if (cliente != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(cliente.getUsername())
                    .password(cliente.getSenha())
                    .roles(cliente.getPerfil().name())
                    .build();
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}