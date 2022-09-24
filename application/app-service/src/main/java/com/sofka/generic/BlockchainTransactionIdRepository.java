package com.sofka.generic;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlockchainTransactionIdRepository {

  Flux<String> getTransactionBlockchainsIDs(String transactionID);

  void saveTransactionBlockchainID(String transactionID, String transactionBlockchainID);
}
