package com.br.cuidaidoso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EnderecoService.class);

    private final EnderecoFeign enderecoFeign;
    private final EnderecoRepository enderecoRepository;

    public EnderecoResponse executa(EnderecoRequest request) {
        logger.info("Recebido EnderecoRequest: {}", request);

        if (!isCepValido(request.getCep())) {
            throw new IllegalArgumentException("CEP inválido: " + request.getCep());
        }

        EnderecoResponse response = enderecoFeign.buscarEnderecoCep(request.getCep());
        logger.info("Resposta do ViaCEP: {}", response);

        Endereco endereco = converteParaEndereco(response);
        enderecoRepository.save(endereco);
        logger.info("Endereço salvo com sucesso: {}", endereco);
        return response;
    }

    private boolean isCepValido(String cep) {
        return cep != null && cep.matches("\\d{8}");
    }

    private Endereco converteParaEndereco(EnderecoResponse response) {
        Endereco endereco = new Endereco();
        endereco.setCep(response.getCep());
        endereco.setUf(response.getUf());
        endereco.setEstado(response.getEstado());
        endereco.setLocalidade(response.getLocalidade());
        endereco.setBairro(response.getBairro());
        return endereco;
    }
}