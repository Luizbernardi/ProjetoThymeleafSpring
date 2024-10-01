package com.br.cuidaidoso.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnderecoResponse {

    private String cep;
    private String uf;
    private String estado;
    private String localidade;

}
