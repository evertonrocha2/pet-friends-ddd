package com.petfriends.agendamento.domain;

import java.util.Optional;
import java.util.UUID;

/**
 * Um repositorio por agregado. O acesso e sempre pela raiz, nunca por uma
 * entidade interna, para que as invariantes continuem garantidas.
 *
 * O metodo existeConflito mostra um limite importante: a regra de que um
 * veterinario nao pode ter duas consultas no mesmo horario atravessa varios
 * agregados, entao ela NAO cabe dentro da raiz. Fica aqui, apoiada por um
 * indice unico no banco.
 */
public interface ConsultaRepository {

    Optional<Consulta> porId(UUID id);

    boolean existeConflito(VeterinarioId veterinarioId, Periodo periodo);

    Consulta salvar(Consulta consulta);
}
