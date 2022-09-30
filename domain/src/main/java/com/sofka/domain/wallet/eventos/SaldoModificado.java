package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class SaldoModificado extends DomainEvent {

  private final WalletID walletID;

  private final Cantidad cantidad;

  private final TransferenciaID transferenciaID;

  public SaldoModificado(WalletID walletID, Cantidad cantidad, TransferenciaID transferenciaID) {
    super("com.sofka.domain.wallet.eventos.SaldoModificado");
    this.walletID = walletID;
    this.cantidad = cantidad;
    this.transferenciaID = transferenciaID;
  }

  public WalletID getWalletID() {
    return walletID;
  }

  public Cantidad getCantidad() {
    return cantidad;
  }

  public TransferenciaID getTransferenciaID() {
    return transferenciaID;
  }

  @Override
  public String toString() {
    return "SaldoModificado{" +
        "walletID=" + walletID +
        ", cantidad=" + cantidad +
        '}';
  }
}
