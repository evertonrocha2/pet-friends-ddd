package com.petfriends.scheduling.domain;

import com.petfriends.scheduling.domain.event.BookingConfirmed;
import com.petfriends.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Raiz do agregado Agendamento do Pet Friends.
 *
 * Pet e Caregiver sao OUTROS agregados, cada um com ciclo de vida proprio.
 * Por isso entram aqui apenas como identidade (PetId, CaregiverId) e nunca
 * como objeto completo: assim uma transacao altera um unico agregado e a
 * fronteira de consistencia fica evidente.
 */
public class Booking {

    public enum Status { REQUESTED, CONFIRMED, DONE, CANCELLED }

    private final UUID id;
    private final PetId petId;             // outro agregado, so o ID
    private final CaregiverId caregiverId; // outro agregado, so o ID
    private final TimeSlot slot;
    private final Money price;
    private Status status;

    private final List<DomainEvent> pendingEvents = new ArrayList<>();

    public Booking(PetId petId, CaregiverId caregiverId, TimeSlot slot, Money price) {
        if (slot.start().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("nao e possivel agendar no passado");
        }
        this.id = UUID.randomUUID();
        this.petId = petId;
        this.caregiverId = caregiverId;
        this.slot = slot;
        this.price = price;
        this.status = Status.REQUESTED;
    }

    /**
     * Metodo de negocio: confirma o agendamento e REGISTRA o evento de dominio.
     * O agregado nao publica nada. Ele so declara o fato; a publicacao fica com
     * a camada de aplicacao, depois que a transacao tiver sido gravada.
     */
    public void confirm() {
        if (status != Status.REQUESTED) {
            throw new IllegalStateException("so agendamento solicitado pode ser confirmado");
        }
        this.status = Status.CONFIRMED;
        this.pendingEvents.add(
                new BookingConfirmed(id, petId, caregiverId, slot.start(), price));
    }

    public void cancel() {
        if (status == Status.DONE) {
            throw new IllegalStateException("atendimento realizado nao pode ser cancelado");
        }
        this.status = Status.CANCELLED;
    }

    /** Entrega os eventos acumulados e limpa a lista. Chamado pela aplicacao. */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> copy = List.copyOf(pendingEvents);
        pendingEvents.clear();
        return copy;
    }

    public UUID id() { return id; }

    public PetId petId() { return petId; }

    public Status status() { return status; }
}
