package com.petfriends.agendamento.domain;

import java.util.UUID;

/**
 * Referencia ao agregado Pet, que vive no contexto de Tutores e Pets.
 *
 * A Consulta guarda o ID, e nunca o objeto Pet inteiro. Isso mantem a
 * fronteira transacional pequena e impede que uma unica transacao altere
 * dois agregados ao mesmo tempo.
 */
public record PetId(UUID valor) {

    public PetId {
        if (valor == null) {
            throw new IllegalArgumentException("petId e obrigatorio");
        }
    }

    public static PetId de(String bruto) {
        return new PetId(UUID.fromString(bruto));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
