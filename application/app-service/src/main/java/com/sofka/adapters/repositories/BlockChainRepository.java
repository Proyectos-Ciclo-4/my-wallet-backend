package com.sofka.adapters.repositories;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.BlockchainRepository;
import com.sofka.generic.Blockchain;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BlockChainRepository implements BlockchainRepository {

  private final Blockchain blockchain;

  public BlockChainRepository(Blockchain blockchain) {
    this.blockchain = blockchain;
  }

  @Override
  public void saveTransaction(DomainEvent event) {
    blockchain.postEventToBlockchain(event);
  }

  @Override
  public Mono<DomainEvent> getFromBlockchain(String id) {
    return null;
  }

  @Override
  public Mono<String> getTransactionBlockchainID(String transactionID) {
    return null;
  }
}
