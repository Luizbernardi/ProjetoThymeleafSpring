package com.br.cuidaidoso.enums;

public enum Perfil {

    ADMIN("Administrador"),
    CUIDADOR("Cuidador");

    private String perfil;

    private Perfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}
