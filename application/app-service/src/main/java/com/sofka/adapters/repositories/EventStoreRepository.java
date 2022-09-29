package com.sofka.adapters.repositories;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.Usuario;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import com.sofka.generic.materialize.model.SavedHash;
import java.util.Comparator;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EventStoreRepository implements com.sofka.generic.EventStoreRepository {

  private final ReactiveMongoTemplate template;

  private final StoredEvent.EventSerializer eventSerializer;

  public EventStoreRepository(ReactiveMongoTemplate template, EventSerializer eventSerializer) {
    this.template = template;
    this.eventSerializer = eventSerializer;
  }

  public Mono<Boolean> userExists(String email, String telefono) {
    return template.exists(Query.query(Criteria.where("numero").is(telefono)), Usuario.class,
        "usuarios").flatMap(aBoolean -> aBoolean ? Mono.just(aBoolean)
        : template.exists(Query.query(Criteria.where("email").is(email)), Usuario.class,
            "usuarios"));
  }

  @Override
  public Flux<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
    var query = new Query(Criteria.where("aggregateRootId").is(aggregateRootId));

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

  public Mono<Boolean> walletExists(String id) {

    return template.exists(Query.query(Criteria.where("walletId").is(id)), "wallet_data");
  }

  @Override
  public Mono<SavedHash> saveEventHash(String hash, String typeName) {
    return template.save(new SavedHash(hash, typeName), "hashes");
  }
}