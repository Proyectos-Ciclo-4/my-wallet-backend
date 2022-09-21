package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;

public class TransferenciaFallida extends DomainEvent {

  private final TransferenciaID transferenciaID;

  public TransferenciaFallida(TransferenciaID transferenciaID
  ) {
    super("com.sofka.domain.wallet.TransferenciaFallida");
    this.transferenciaID = transferenciaID;
  }

  public TransferenciaID getTransferenciaID() {
    return transferenciaID;
  }
}