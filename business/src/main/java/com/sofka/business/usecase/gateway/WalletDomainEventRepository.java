package com.sofka.business.usecase.gateway;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletDomainEventRepository {

  Flux<DomainEvent> obtenerEventos(String id);

  Mono<Boolean> exists(String id);

}