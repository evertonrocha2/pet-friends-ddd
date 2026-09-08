# pet-friends-ddd

Domain-Driven Design study for **Pet Friends**, a pet care booking platform where tutors schedule services such as bathing, grooming and veterinary visits with caregivers.

This repository holds the Java code written for the DDD assignment: aggregates, business invariants, domain events and the architecture that publishes them.

## Domain

The platform is split into bounded contexts. This repository focuses on **Scheduling**.

| Concept | Type | Note |
| --- | --- | --- |
| Booking | Aggregate root | Owns the appointment lifecycle and its invariants |
| PetId, CaregiverId | Value objects | References to other aggregates, by identity only |
| TimeSlot, Money | Value objects | Immutable, compared by value |
| BookingConfirmed | Domain event | A fact that already happened |

Pet and Caregiver are separate aggregates with their own lifecycles, so Booking stores only their identifiers. One transaction changes one aggregate.

## Structure

    src/main/java/com/petfriends
    |-- shared/domain
    |   |-- DomainEvent.java              abstraction for any domain event
    |   +-- DomainEventPublisher.java     outbound port, hides the broker
    +-- scheduling
        |-- domain
        |   |-- Booking.java              aggregate root
        |   |-- BookingRepository.java    one repository per aggregate
        |   |-- PetId.java                reference to another aggregate
        |   |-- CaregiverId.java          reference to another aggregate
        |   |-- TimeSlot.java             value object
        |   |-- Money.java                value object
        |   +-- event
        |       +-- BookingConfirmed.java  domain event implementation
        +-- application
            +-- ConfirmBookingService.java  use case: save first, publish after

## Assignment mapping

| Question | File |
| --- | --- |
| 6. Aggregate that references another aggregate by id | [Booking.java](src/main/java/com/petfriends/scheduling/domain/Booking.java) |
| 7. Business method that publishes a domain event | [Booking.confirm()](src/main/java/com/petfriends/scheduling/domain/Booking.java) |
| 9. Domain event abstraction | [DomainEvent.java](src/main/java/com/petfriends/shared/domain/DomainEvent.java) |
| 10. Domain event implementation | [BookingConfirmed.java](src/main/java/com/petfriends/scheduling/domain/event/BookingConfirmed.java) |

## Event flow

Booking.confirm() records the event inside the aggregate. ConfirmBookingService saves the aggregate and only then publishes, so a fact is never announced before the database confirms it. In production the pair becomes the Outbox pattern: state and event are written in the same transaction, a worker reads the outbox and delivers to the broker topic petfriends.scheduling.booking.confirmed, which fans out to the notification, caregiver agenda and billing queues.

## Course

Domain-Driven Design (DDD) e Arquitetura de Softwares Escalaveis com Java, Instituto Infnet.
