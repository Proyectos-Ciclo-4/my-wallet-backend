package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.materialize.model.SavedHash;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlockchainRepository {

  void saveTransaction(DomainEvent event);

  Mono<Void> getFromBlockchain(Flux<SavedHash> hash);

  Mono<String> getTransactionBlockchainID(String transactionID);

}
