package com.petfriends.scheduling.domain;

import java.util.UUID;

/**
 * Referencia ao agregado Pet, que tem ciclo de vida proprio.
 *
 * O agendamento guarda o ID, e nunca o objeto Pet inteiro. Isso mantem a
 * fronteira transacional do agregado Booking pequena e impede que uma
 * unica transacao altere dois agregados ao mesmo tempo.
 */
public record PetId(UUID value) {

    public PetId {
        if (value == null) {
            throw new IllegalArgumentException("petId e obrigatorio");
        }
    }

    public static PetId of(String raw) {
        return new PetId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
