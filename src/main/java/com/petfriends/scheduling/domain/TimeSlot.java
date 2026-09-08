package com.petfriends.scheduling.domain;

import java.time.LocalDateTime;

/** Objeto de valor com a janela do atendimento. */
public record TimeSlot(LocalDateTime start, LocalDateTime end) {

    public TimeSlot {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("fim deve ser depois do inicio");
        }
    }

    public boolean overlaps(TimeSlot other) {
        return start.isBefore(other.end) && other.start.isBefore(end);
    }
}
