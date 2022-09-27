package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import lombok.Getter;

@Getter
public class TransferenciaValidada extends DomainEvent {

  private final TransferenciaID transferenciaID;

  public TransferenciaValidada(TransferenciaID transferenciaID) {
    super("com.sofka.domain.wallet.eventos.TransferenciaValidada");
    this.transferenciaID = transferenciaID;
  }
}
