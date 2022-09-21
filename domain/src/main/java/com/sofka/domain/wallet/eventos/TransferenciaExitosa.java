package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;

public class TransferenciaExitosa extends DomainEvent {

  private final TransferenciaID transferenciaID;

  private Cantidad cantidad = new Cantidad(0.0);

  public TransferenciaExitosa(TransferenciaID transferenciaID,
      Cantidad cantidad) {
    super("com.sofka.domain.wallet.TransferenciaExitosa");
    this.transferenciaID = transferenciaID;
    this.cantidad = cantidad;
  }

  public TransferenciaExitosa(TransferenciaID transferenciaID
      ) {
    super("com.sofka.domain.wallet.TransferenciaExitosa");
    this.transferenciaID = transferenciaID;
  }

  public TransferenciaID getTransferenciaID() {
    return transferenciaID;
  }

  public Cantidad getCantidad() {
    return cantidad;
  }
}