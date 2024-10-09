package com.br.cuidaidoso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cuidador extends User {

    @Size(min = 3, max = 100)
    private String formacao;

    @ManyToOne
    private Endereco endereco;

    @OneToMany(mappedBy = "cuidador")
    private List<CuidadorLog> cuidadorLog = new ArrayList<>();

}
