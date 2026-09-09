package com.petfriends.shared.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstracao de evento de dominio no Pet Friends.
 *
 * Um evento de dominio e o registro de um fato que JA aconteceu, por isso as
 * implementacoes sao sempre nomeadas no passado: ConsultaAgendada,
 * ConsultaCancelada, RegistroCfmvSuspenso. O evento e imutavel: so leitura,
 * nunca setter.
 */
public interface EventoDeDominio {

    /** Identidade do evento. E com ela que o consumidor descarta duplicatas. */
    UUID eventoId();

    /** Momento em que o fato ocorreu no dominio. */
    Instant ocorridoEm();

    /** Id do agregado de origem. Serve de chave de particionamento. */
    String agregadoId();

    /** Nome do tipo. Usado no roteamento e na Event Store. */
    String tipo();

    /** Versao do contrato, para evoluir sem quebrar quem ja escuta. */
    default int versaoDoContrato() {
        return 1;
    }
}
