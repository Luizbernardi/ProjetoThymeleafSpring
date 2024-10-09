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
public class Admin extends User {

    @ManyToOne
    private Endereco endereco;

    @OneToMany(mappedBy = "admin")
    private List<AdminLog> adminLog = new ArrayList<>();

}
