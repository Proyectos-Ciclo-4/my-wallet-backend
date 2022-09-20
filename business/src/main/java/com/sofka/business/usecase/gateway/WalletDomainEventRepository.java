package com.sofka.business.usecase.gateway;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;

public interface WalletDomainEventRepository {
  Flux<DomainEvent> obtenerEventos(String id);

}
