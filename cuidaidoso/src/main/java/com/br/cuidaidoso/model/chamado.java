package com.br.cuidaidoso.model;

import java.time.LocalDate;

import com.br.cuidaidoso.enums.Prioridade;
import com.br.cuidaidoso.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;

    private LocalDate dataAbertura = LocalDate.now();

    private LocalDate dataFechamento;

    private String descricao;

    private Status status;

    private Prioridade prioridade;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "cuidador_id")
    private Cuidador cuidador;

}
