package com.petfriends.agendamento.domain.evento;

import com.petfriends.agendamento.domain.Dinheiro;
import com.petfriends.agendamento.domain.PetId;
import com.petfriends.agendamento.domain.VeterinarioId;
import com.petfriends.shared.domain.EventoDeDominio;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementacao concreta: o fato "a consulta foi agendada".
 *
 * E este evento que o contexto de Gestao de Veterinarios assina para saber
 * que o horario do profissional foi ocupado, e que o contexto de Atendimento
 * usa para avisar o tutor. O record ja garante a imutabilidade, e o eventoId
 * permite descartar uma entrega duplicada sem criar duas consultas.
 */
public record ConsultaAgendada(UUID eventoId,
                               Instant ocorridoEm,
                               UUID consultaId,
                               PetId petId,
                               VeterinarioId veterinarioId,
                               LocalDateTime inicio,
                               Dinheiro valor) implements EventoDeDominio {

    /** Construtor de conveniencia: o dominio so informa o fato. */
    public ConsultaAgendada(UUID consultaId,
                            PetId petId,
                            VeterinarioId veterinarioId,
                            LocalDateTime inicio,
                            Dinheiro valor) {
        this(UUID.randomUUID(), Instant.now(), consultaId, petId,
             veterinarioId, inicio, valor);
    }

    @Override
    public String agregadoId() {
        return consultaId.toString();
    }

    @Override
    public String tipo() {
        return "petfriends.agendamento.consulta.agendada";
    }
}
