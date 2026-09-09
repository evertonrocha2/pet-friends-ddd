package com.petfriends.agendamento.domain;

import java.time.LocalDateTime;

/**
 * Objeto de valor com a janela da consulta.
 *
 * Imutavel, sem identidade e comparado pelo conteudo: duas janelas com o
 * mesmo inicio e o mesmo fim sao a mesma coisa.
 */
public record Periodo(LocalDateTime inicio, LocalDateTime fim) {

    public Periodo {
        if (inicio == null || fim == null || !fim.isAfter(inicio)) {
            throw new IllegalArgumentException("o fim deve ser depois do inicio");
        }
    }

    public boolean conflitaCom(Periodo outro) {
        return inicio.isBefore(outro.fim) && outro.inicio.isBefore(fim);
    }
}
