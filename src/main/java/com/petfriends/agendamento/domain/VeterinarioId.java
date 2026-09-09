package com.petfriends.agendamento.domain;

import java.util.UUID;

/**
 * Referencia ao agregado Veterinario, do contexto de Gestao de Veterinarios.
 *
 * Quem valida o registro no CFMV e aquele contexto, nao este. Para o
 * Agendamento basta a identidade do profissional que vai atender.
 */
public record VeterinarioId(UUID valor) {

    public VeterinarioId {
        if (valor == null) {
            throw new IllegalArgumentException("veterinarioId e obrigatorio");
        }
    }

    public static VeterinarioId de(String bruto) {
        return new VeterinarioId(UUID.fromString(bruto));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
