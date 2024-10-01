package com.br.cuidaidoso.service;

import org.springframework.stereotype.Service;

import com.br.cuidaidoso.feign.EnderecoFeign;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.model.dto.EnderecoResponse;
import com.br.cuidaidoso.repository.EnderecoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EnderecoService {

    private final EnderecoFeign enderecoFeign;
    private final EnderecoRepository enderecoRepository;

    public EnderecoResponse executa(EnderecoRequest request) {
        EnderecoResponse response = enderecoFeign.buscarEnderecoCep(request.getCep());
        Endereco endereco = converteParaEndereco(response);
        enderecoRepository.save(endereco);
        return response;
    }

    private Endereco converteParaEndereco(EnderecoResponse response) {
        Endereco endereco = new Endereco();
        endereco.setCep(response.getCep());
        endereco.setUf(response.getUf());
        endereco.setEstado(response.getEstado());
        endereco.setLocalidade(response.getLocalidade());
        return endereco;
    }
}
