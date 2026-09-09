# pet-friends-ddd

Código do TP2 de Domain-Driven Design: agregados, invariantes de negócio, eventos de domínio e event sourcing, dentro do escopo do projeto **Pet Friends**.

## Contexto

O Pet Friends é uma plataforma de serviços e produtos para pets, dividida em vários contextos delimitados. Este repositório vive no contexto de **Agendamento de Serviços**, que é core, e conversa com **Gestão de Veterinários** por eventos de domínio.

| Conceito | Tipo | Observação |
| --- | --- | --- |
| Consulta | Raiz do agregado | Dona do ciclo de vida do atendimento e das suas invariantes |
| PetId, VeterinarioId | Objetos de valor | Referência a outros agregados, apenas pela identidade |
| Periodo, Dinheiro | Objetos de valor | Imutáveis, comparados pelo conteúdo |
| ConsultaAgendada, ConsultaCancelada | Eventos de domínio | Fatos que já aconteceram |

Pet e Veterinário são agregados de outros contextos, cada um com ciclo de vida próprio. Por isso a Consulta guarda só o identificador deles, e uma transação altera um único agregado.

## Estrutura

    src/main/java/com/petfriends
    |-- shared/domain
    |   |-- EventoDeDominio.java            abstração de qualquer evento
    |   +-- PublicadorDeEventos.java        porta de saída, esconde o broker
    +-- agendamento
        |-- domain
        |   |-- Consulta.java               raiz do agregado
        |   |-- ConsultaRepository.java     um repositório por agregado
        |   |-- PetId.java                  referência a outro agregado
        |   |-- VeterinarioId.java          referência a outro agregado
        |   |-- Periodo.java                objeto de valor
        |   |-- Dinheiro.java               objeto de valor
        |   +-- evento
        |       |-- ConsultaAgendada.java
        |       +-- ConsultaCancelada.java
        +-- application
            +-- CancelarConsultaService.java  caso de uso: grava, depois publica

## Mapeamento das questões do TP2

| Questão | Arquivo |
| --- | --- |
| 6. Agregado que referencia outro agregado por ID | [Consulta.java](src/main/java/com/petfriends/agendamento/domain/Consulta.java) |
| 7. Método de negócio que publica um evento de domínio | [Consulta.cancelar()](src/main/java/com/petfriends/agendamento/domain/Consulta.java) e [CancelarConsultaService.java](src/main/java/com/petfriends/agendamento/application/CancelarConsultaService.java) |
| 9. Abstração de evento de domínio | [EventoDeDominio.java](src/main/java/com/petfriends/shared/domain/EventoDeDominio.java) |
| 10. Implementação de evento de domínio | [ConsultaAgendada.java](src/main/java/com/petfriends/agendamento/domain/evento/ConsultaAgendada.java) |

## Fluxo do evento

A Consulta registra o fato dentro do agregado. O caso de uso grava o estado e só então publica, para nunca anunciar algo que o banco não confirmou. Em produção o par vira o padrão Outbox: estado e evento gravados na mesma transação, um worker lê a outbox e entrega ao tópico petfriends.agendamento.consulta.agendada, que faz fan-out para as filas de Gestão de Veterinários, Atendimento e Faturamento.

Cada consumidor usa o eventoId para descartar entrega duplicada, de modo que a mesma notificação recebida duas vezes nunca gera duas consultas.

## Disciplina

Domain-Driven Design (DDD) e Arquitetura de Softwares Escaláveis com Java, Instituto Infnet.
