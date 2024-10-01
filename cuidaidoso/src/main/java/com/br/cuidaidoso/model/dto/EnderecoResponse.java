package com.br.cuidaidoso.model.dto;

import lombok.Data;

@Data
public class EnderecoResponse {

    private String cep;
    private String uf;
    private String estado;
    private String localidade;
    private String bairro;

}
