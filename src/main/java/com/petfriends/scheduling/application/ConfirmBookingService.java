package com.petfriends.scheduling.application;

import com.petfriends.scheduling.domain.Booking;
import com.petfriends.scheduling.domain.BookingRepository;
import com.petfriends.shared.domain.DomainEventPublisher;

import java.util.UUID;

/**
 * Caso de uso: uma transacao, um agregado.
 *
 * A ordem importa. Primeiro grava o estado, depois publica os eventos. Assim
 * nunca se anuncia um fato que o banco nao confirmou. Em producao esse par
 * vira o padrao Outbox: estado e evento gravados na MESMA transacao e um
 * worker le a outbox e entrega ao broker.
 */
public class ConfirmBookingService {

    private final BookingRepository bookings;
    private final DomainEventPublisher publisher;

    public ConfirmBookingService(BookingRepository bookings,
                                 DomainEventPublisher publisher) {
        this.bookings = bookings;
        this.publisher = publisher;
    }

    public void handle(UUID bookingId) {
        Booking booking = bookings.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("agendamento nao encontrado"));

        booking.confirm();

        bookings.save(booking);
        publisher.publish(booking.pullDomainEvents());
    }
}
