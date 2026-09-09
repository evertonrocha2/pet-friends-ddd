package com.petfriends.agendamento.application;

import com.petfriends.agendamento.domain.Consulta;
import com.petfriends.agendamento.domain.ConsultaRepository;
import com.petfriends.shared.domain.PublicadorDeEventos;

import java.util.UUID;

/**
 * Caso de uso: uma transacao, um agregado.
 *
 * A ordem importa. Primeiro grava o estado, depois publica os eventos. Assim
 * nunca se anuncia um fato que o banco nao confirmou. Em producao esse par
 * vira o padrao Outbox: estado e evento gravados na MESMA transacao e um
 * worker le a outbox e entrega ao broker.
 */
public class CancelarConsultaService {

    private final ConsultaRepository consultas;
    private final PublicadorDeEventos publicador;

    public CancelarConsultaService(ConsultaRepository consultas,
                                   PublicadorDeEventos publicador) {
        this.consultas = consultas;
        this.publicador = publicador;
    }

    public void executar(UUID consultaId, String motivo) {
        Consulta consulta = consultas.porId(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("consulta nao encontrada"));

        consulta.cancelar(motivo);

        consultas.salvar(consulta);
        publicador.publicar(consulta.puxarEventos());
    }
}
