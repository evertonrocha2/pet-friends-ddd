package com.petfriends.agendamento.domain;

import java.math.BigDecimal;

/** Objeto de valor: imutavel, sem identidade, comparado pelo conteudo. */
public record Dinheiro(BigDecimal quantia, String moeda) {

    public Dinheiro {
        if (quantia == null || quantia.signum() < 0) {
            throw new IllegalArgumentException("a quantia nao pode ser negativa");
        }
    }

    public static Dinheiro reais(String quantia) {
        return new Dinheiro(new BigDecimal(quantia), "BRL");
    }

    public Dinheiro somar(Dinheiro outro) {
        if (!moeda.equals(outro.moeda)) {
            throw new IllegalArgumentException("moedas diferentes");
        }
        return new Dinheiro(quantia.add(outro.quantia), moeda);
    }
}
