package com.sofka.handle;

import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.generic.Blockchain;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class TransactionEventHandler {
  private final Blockchain blockchain;

  public TransactionEventHandler(Blockchain blockchain) {
    this.blockchain = blockchain;
  }

  @EventListener
  public void onTransactionCreated(TransferenciaCreada event) {
    blockchain.postEventToBlockchain(event);
  }
}
