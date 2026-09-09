package com.petfriends.agendamento.domain;

import com.petfriends.agendamento.domain.evento.ConsultaAgendada;
import com.petfriends.agendamento.domain.evento.ConsultaCancelada;
import com.petfriends.shared.domain.EventoDeDominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Raiz do agregado Consulta, no contexto de Agendamento de Servicos.
 *
 * Pet e Veterinario sao OUTROS agregados, cada um no seu contexto e com ciclo
 * de vida proprio. Por isso entram aqui apenas como identidade (PetId,
 * VeterinarioId) e nunca como objeto completo: assim uma transacao altera um
 * unico agregado e a fronteira de consistencia fica evidente.
 */
public class Consulta {

    public enum Situacao { AGENDADA, REALIZADA, CANCELADA }

    private final UUID id;
    private final PetId petId;                 // outro agregado, so o ID
    private final VeterinarioId veterinarioId; // outro agregado, so o ID
    private final Periodo periodo;
    private final Dinheiro valor;
    private Situacao situacao;

    private final List<EventoDeDominio> eventosPendentes = new ArrayList<>();

    public Consulta(PetId petId, VeterinarioId veterinarioId,
                    Periodo periodo, Dinheiro valor) {
        if (periodo.inicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("nao e possivel agendar no passado");
        }
        this.id = UUID.randomUUID();
        this.petId = petId;
        this.veterinarioId = veterinarioId;
        this.periodo = periodo;
        this.valor = valor;
        this.situacao = Situacao.AGENDADA;
        this.eventosPendentes.add(
                new ConsultaAgendada(id, petId, veterinarioId, periodo.inicio(), valor));
    }

    /**
     * Metodo de negocio: cancela a consulta e REGISTRA o evento de dominio.
     *
     * A invariante e conferida primeiro. So depois o estado muda e o fato e
     * registrado. O agregado nao publica nada: ele apenas declara o que
     * aconteceu. Publicar e papel da camada de aplicacao, depois do commit.
     */
    public void cancelar(String motivo) {
        if (situacao == Situacao.REALIZADA) {
            throw new IllegalStateException("consulta ja realizada nao pode ser cancelada");
        }
        if (situacao == Situacao.CANCELADA) {
            throw new IllegalStateException("consulta ja esta cancelada");
        }
        this.situacao = Situacao.CANCELADA;
        this.eventosPendentes.add(
                new ConsultaCancelada(id, veterinarioId, periodo.inicio(), motivo));
    }

    public void registrarAtendimento() {
        if (situacao != Situacao.AGENDADA) {
            throw new IllegalStateException("so consulta agendada pode ser realizada");
        }
        this.situacao = Situacao.REALIZADA;
    }

    /** Entrega os eventos acumulados e limpa a lista. Chamado pela aplicacao. */
    public List<EventoDeDominio> puxarEventos() {
        List<EventoDeDominio> copia = List.copyOf(eventosPendentes);
        eventosPendentes.clear();
        return copia;
    }

    public UUID id() { return id; }

    public PetId petId() { return petId; }

    public VeterinarioId veterinarioId() { return veterinarioId; }

    public Periodo periodo() { return periodo; }

    public Situacao situacao() { return situacao; }
}
