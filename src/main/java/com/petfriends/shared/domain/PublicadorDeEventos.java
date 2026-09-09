package com.petfriends.shared.domain;

import java.util.List;

/**
 * Porta de saida do dominio para publicacao de eventos.
 *
 * O agregado nao conhece Kafka, RabbitMQ nem banco de dados. Ele apenas
 * registra o que aconteceu. Como isso vira mensagem e problema da
 * infraestrutura, escondida atras desta interface.
 */
public interface PublicadorDeEventos {

    void publicar(List<EventoDeDominio> eventos);
}
package com.petfriends.shared.domain;

import java.util.List;

/**
 * Porta de saida do dominio para publicacao de eventos.
 *
 * O agregado nao conhece Kafka, RabbitMQ nem banco de dados. Ele apenas
 * registra o que aconteceu. Como isso vira mensagem e problema da
 * infraestrutura, escondida atras desta interface.
 */
public interface DomainEventPublisher {

    void publish(List<DomainEvent> events);
}
