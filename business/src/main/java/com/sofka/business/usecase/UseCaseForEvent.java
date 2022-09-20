package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class UseCaseForEvent<R extends DomainEvent> implements
    Function<Mono<R>, Flux<DomainEvent>> {

  public abstract Flux<DomainEvent> apply(Mono<R> rMono);
}
