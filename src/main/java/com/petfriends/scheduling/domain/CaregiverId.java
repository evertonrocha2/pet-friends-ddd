package com.petfriends.scheduling.domain;

import java.util.UUID;

/**
 * Referencia ao agregado Caregiver, o prestador que executa o servico.
 * Mesma regra do PetId: agregado externo entra so como identidade.
 */
public record CaregiverId(UUID value) {

    public CaregiverId {
        if (value == null) {
            throw new IllegalArgumentException("caregiverId e obrigatorio");
        }
    }

    public static CaregiverId of(String raw) {
        return new CaregiverId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
