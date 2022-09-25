package com.sofka.adapters.repositories;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WalletEventRepository implements WalletDomainEventRepository {

  private final EventStoreRepository repository;

  public WalletEventRepository(EventStoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> obtenerEventos(String id) {
    return repository.getEventsBy("wallet", id);
  }

  @Override
  public Mono<Boolean> exists(String id) {
    return repository.walletExists(id);
  }

}