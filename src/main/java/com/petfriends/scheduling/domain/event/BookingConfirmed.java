package com.petfriends.scheduling.domain.event;

import com.petfriends.scheduling.domain.CaregiverId;
import com.petfriends.scheduling.domain.Money;
import com.petfriends.scheduling.domain.PetId;
import com.petfriends.shared.domain.DomainEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementacao concreta de evento de dominio no Pet Friends.
 *
 * Record garante a imutabilidade de graca. O payload leva so o necessario
 * para quem escuta: notificacao avisa o tutor, agenda bloqueia o horario do
 * prestador e faturamento gera a cobranca.
 */
public record BookingConfirmed(UUID eventId,
                               Instant occurredOn,
                               UUID bookingId,
                               PetId petId,
                               CaregiverId caregiverId,
                               LocalDateTime scheduledFor,
                               Money price) implements DomainEvent {

    /** Construtor de conveniencia: o dominio so informa o fato. */
    public BookingConfirmed(UUID bookingId,
                            PetId petId,
                            CaregiverId caregiverId,
                            LocalDateTime scheduledFor,
                            Money price) {
        this(UUID.randomUUID(), Instant.now(), bookingId, petId,
             caregiverId, scheduledFor, price);
    }

    @Override
    public String aggregateId() {
        return bookingId.toString();
    }

    @Override
    public String eventType() {
        return "petfriends.scheduling.booking.confirmed";
    }
}
