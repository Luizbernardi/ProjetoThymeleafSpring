package com.br.cuidaidoso.model;

import java.time.LocalDate;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "O nome deve conter apenas letras")
    private String nome;

    @NotNull
    @Email(message = "O email deve ser válido")
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z\\d]).+$", message = "A senha deve conter pelo menos uma letra maiúscula e um caractere especial")
    private String senha;

    private String imagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotNull
    @Size(min = 11)
    @Pattern(regexp = "\\d{11}", message = "O telefone deve conter apenas dígitos")
    private String telefone;

    @NotNull
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas dígitos")
    @Column(unique = true)
    private String cpf;

    @NotNull
    @Past(message = "A data de nascimento deve ser uma data no passado")
    private LocalDate dataNascimento;

}