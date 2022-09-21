package com.sofka.generic.handle;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.StoredEvent;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class IntegrationHandle implements Function<Flux<DomainEvent>, Mono<Void>> {
  private final EventStoreRepository repository;

  private final StoredEvent.EventSerializer eventSerializer;

  private final EventBus eventBus;

  @Override
  public Mono<Void> apply(Flux<DomainEvent> domainEventFlux) {
    return null;
  }
}