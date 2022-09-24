package com.sofka.business.usecase.gateway;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Mono;

public interface BlockchainRepository {

  Mono<DomainEvent> getFromBlockchain(String id);

  Mono<String> getTransactionBlockchainID(String transactionID);

}
