package com.br.cuidaidoso.enums;

public enum Status {

    ABERTO("Aberto"),
    ANDAMENTO("Em Andamento"),
    FECHADO("Fechado");

    private String chamadoTicket;

    private Status(String chamadoTicket) {
        this.chamadoTicket = chamadoTicket;
    }

    public String getChamadoTicket() {
        return chamadoTicket;
    }

    public void setChamadoTicket(String chamadoTicket) {
        this.chamadoTicket = chamadoTicket;
    }

}