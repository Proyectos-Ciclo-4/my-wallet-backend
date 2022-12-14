package com.sofka.handle;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.BlockchainRepository;
import com.sofka.generic.EventBus;
import com.sofka.generic.EventStoreRepository;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.util.function.Function;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RegisterIntegrationHandle implements Function<Flux<DomainEvent>, Mono<Void>> {

  private final EventStoreRepository repository;

  private final EventSerializer eventSerializer;

  private final BlockchainRepository blockchainRepository;

  private final EventBus eventBus;

  private final ApplicationEventPublisher applicationEventPublisher;


  public RegisterIntegrationHandle(EventStoreRepository repository, EventSerializer eventSerializer,
      BlockchainRepository blockchainRepository, EventBus eventBus,
      ApplicationEventPublisher applicationEventPublisher) {

    this.repository = repository;
    this.eventSerializer = eventSerializer;
    this.blockchainRepository = blockchainRepository;
    this.eventBus = eventBus;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Mono<Void> apply(Flux<DomainEvent> domainEventFlux) {

    return domainEventFlux.flatMap(domainEvent -> {
          var stored = StoredEvent.wrapEvent(domainEvent, eventSerializer);

          return repository.saveEvent("wallet", domainEvent.aggregateRootId(), stored)
              .thenReturn(domainEvent);
        }).doOnNext(eventBus::publishRegister)
        .doOnNext(applicationEventPublisher::publishEvent)
        .doOnNext(blockchainRepository::saveTransaction)
        .then();
  }

  @Override
  public <V> Function<V, Mono<Void>> compose(
      Function<? super V, ? extends Flux<DomainEvent>> before) {
    return Function.super.compose(before);
  }

  @Override
  public <V> Function<Flux<DomainEvent>, V> andThen(
      Function<? super Mono<Void>, ? extends V> after) {
    return Function.super.andThen(after);
  }
}