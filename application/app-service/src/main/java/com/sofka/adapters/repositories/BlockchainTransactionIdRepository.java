package com.sofka.adapters.repositories;

import com.sofka.handle.model.BlockchainTransactionMongoModel;
import java.util.Comparator;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BlockchainTransactionIdRepository implements
    com.sofka.generic.BlockchainTransactionIdRepository {

  private final ReactiveMongoTemplate template;

  public BlockchainTransactionIdRepository(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @Override
  public Flux<String> getTransactionBlockchainsIDs(String walletID) {
    var query = new Query(Criteria.where("walletID").is(walletID));
    return template.find(query, BlockchainTransactionMongoModel.class)
        .map(blockchainTransactionMongoModel -> {
          return blockchainTransactionMongoModel.getBlockchainTransactionID();
        });
  }

  @Override
  public void saveTransactionBlockchainID(String walletID, String transactionBlockchainID) {
      template.save(new BlockchainTransactionMongoModel(walletID,transactionBlockchainID));
  }

}
