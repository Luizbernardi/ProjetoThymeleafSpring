package com.br.cuidaidoso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente extends User {

    @OneToMany(mappedBy = "cliente")
    private List<ClienteLog> clienteLog = new ArrayList<>();

    @ManyToOne
    private Endereco endereco;
}
