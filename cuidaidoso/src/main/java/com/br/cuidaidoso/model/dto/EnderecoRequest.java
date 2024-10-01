package com.br.cuidaidoso.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoRequest {

    private String cep;
    private String uf;
    private String estado;
    private String localidade;
    private String bairro;

}