package com.petfriends.shared.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstracao de um evento de dominio no Pet Friends.
 *
 * Um evento de dominio descreve um fato que JA aconteceu, por isso o nome
 * das implementacoes vem sempre no passado (BookingConfirmed, PetVaccinated).
 * O evento e imutavel: so tem leitura, nunca setter.
 */
public interface DomainEvent {

    /** Identidade do evento. Permite que o consumidor descarte duplicatas. */
    UUID eventId();

    /** Momento em que o fato ocorreu no dominio. */
    Instant occurredOn();

    /** Id do agregado que originou o evento. Serve de chave de particionamento. */
    String aggregateId();

    /** Nome do tipo do evento. Usado no roteamento e na Event Store. */
    String eventType();

    /** Versao do contrato, para permitir evolucao do payload sem quebrar consumidores. */
    default int schemaVersion() {
        return 1;
    }
}
