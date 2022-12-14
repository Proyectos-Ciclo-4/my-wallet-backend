package com.sofka.handle;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.BlockchainRepository;
import com.sofka.generic.EventBus;
import com.sofka.generic.EventStoreRepository;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class IntegrationHandle implements Function<Flux<DomainEvent>, Mono<Void>> {

  private final EventStoreRepository repository;

  private final StoredEvent.EventSerializer eventSerializer;

  private final BlockchainRepository blockchainRepository;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final EventBus eventBus;

  public IntegrationHandle(EventStoreRepository repository, EventSerializer eventSerializer,
      BlockchainRepository blockchainRepository,
      ApplicationEventPublisher applicationEventPublisher, EventBus eventBus) {

    this.repository = repository;
    this.eventSerializer = eventSerializer;
    this.blockchainRepository = blockchainRepository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.eventBus = eventBus;
  }

  @Override
  public Mono<Void> apply(Flux<DomainEvent> domainEventFlux) {
    return domainEventFlux.flatMap(domainEvent -> {
          log.info("Guardando el evento: {}", domainEvent);

          var stored = StoredEvent.wrapEvent(domainEvent, eventSerializer);

          return repository.saveEvent("wallet", domainEvent.aggregateRootId(), stored).log()
              .thenReturn(domainEvent);
        }).doOnNext(eventBus::publish)
        .doOnNext(applicationEventPublisher::publishEvent)
        .doOnNext(blockchainRepository::saveTransaction)
        .doOnNext(domainEvents ->
            log.info("Eventos publicados: {}", domainEvents)).then();
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