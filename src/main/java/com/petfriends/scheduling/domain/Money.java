package com.petfriends.scheduling.domain;

import java.math.BigDecimal;

/** Objeto de valor: imutavel, sem identidade, comparado pelo conteudo. */
public record Money(BigDecimal amount, String currency) {

    public Money {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("valor nao pode ser negativo");
        }
    }

    public static Money brl(String amount) {
        return new Money(new BigDecimal(amount), "BRL");
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("moedas diferentes");
        }
        return new Money(amount.add(other.amount), currency);
    }
}
