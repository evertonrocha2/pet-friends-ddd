package com.petfriends.agendamento.domain.evento;

import com.petfriends.agendamento.domain.VeterinarioId;
import com.petfriends.shared.domain.EventoDeDominio;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * O fato "a consulta foi cancelada".
 *
 * Gestao de Veterinarios assina este evento para liberar de volta o horario
 * do profissional. E o outro lado da Parceria entre os dois contextos.
 */
public record ConsultaCancelada(UUID eventoId,
                                Instant ocorridoEm,
                                UUID consultaId,
                                VeterinarioId veterinarioId,
                                LocalDateTime inicio,
                                String motivo) implements EventoDeDominio {

    public ConsultaCancelada(UUID consultaId,
                             VeterinarioId veterinarioId,
                             LocalDateTime inicio,
                             String motivo) {
        this(UUID.randomUUID(), Instant.now(), consultaId,
             veterinarioId, inicio, motivo);
    }

    @Override
    public String agregadoId() {
        return consultaId.toString();
    }

    @Override
    public String tipo() {
        return "petfriends.agendamento.consulta.cancelada";
    }
}
