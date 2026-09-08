package com.petfriends.scheduling.domain;

import java.util.Optional;
import java.util.UUID;

/**
 * Um repositorio por agregado. O acesso e sempre pela raiz, nunca por
 * uma entidade interna, para que as invariantes continuem garantidas.
 */
public interface BookingRepository {

    Optional<Booking> findById(UUID id);

    Booking save(Booking booking);
}
