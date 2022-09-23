package com.sofka.generic;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlockchainTransactionIdRepository {

  Flux<String> getTransactionsIDs(String usuarioID);

  void saveTransactionID(String userID, String transactionID);
}
