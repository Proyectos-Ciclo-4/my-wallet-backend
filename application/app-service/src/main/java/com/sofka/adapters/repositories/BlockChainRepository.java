package com.sofka.adapters.repositories;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.Blockchain;
import com.sofka.generic.BlockchainRepository;
import com.sofka.generic.materialize.model.SavedHash;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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
  public Mono<Void> getFromBlockchain(Flux<SavedHash> hash) {
    blockchain.getTransactionHistory(hash);

    return Mono.empty();
  }

  @Override
  public Mono<String> getTransactionBlockchainID(String transactionID) {
    return null;
  }
}
