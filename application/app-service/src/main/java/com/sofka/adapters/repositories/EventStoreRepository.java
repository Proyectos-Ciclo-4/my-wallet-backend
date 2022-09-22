package com.sofka.adapters.repositories;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.util.Comparator;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EventStoreRepository implements com.sofka.generic.EventStoreRepository {

  private final ReactiveMongoTemplate template;

  private final StoredEvent.EventSerializer eventSerializer;

  public EventStoreRepository(ReactiveMongoTemplate template, EventSerializer eventSerializer) {
    this.template = template;
    this.eventSerializer = eventSerializer;
  }

  @Override
  public Flux<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
    var query = new Query(Criteria.where("_id").is(aggregateRootId));

    return template.find(query, DocumentEventStored.class, aggregateName)
        .sort(Comparator.comparing(event -> event.getStoredEvent().getOccurredOn()))
        .map(event -> event.getStoredEvent().deserializeEvent(eventSerializer));
  }

  @Override
  public Mono<Void> saveEvent(String aggregateName, String aggregateRootId,
      StoredEvent storedEvent) {
    var documentEventStored = new DocumentEventStored();
    documentEventStored.setAggregateRootId(aggregateRootId);
    documentEventStored.setStoredEvent(storedEvent);

    return template.save(documentEventStored, aggregateName).then();
  }
}