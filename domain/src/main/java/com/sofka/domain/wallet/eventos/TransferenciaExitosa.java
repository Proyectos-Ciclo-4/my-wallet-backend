package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class TransferenciaExitosa extends DomainEvent {

  private final WalletID walletID;

  private final TransferenciaID transferenciaID;

  private final Estado estadoDeTransferencia;

  public TransferenciaExitosa(WalletID walletID, TransferenciaID transferenciaID,
      Estado estadoDeTransferencia) {
    super("com.sofka.domain.wallet.TransferenciaExitosa");
    this.walletID = walletID;
    this.transferenciaID = transferenciaID;
    this.estadoDeTransferencia = estadoDeTransferencia;
  }

  public WalletID getWalletID() {
    return walletID;
  }

  public TransferenciaID getTransferenciaID() {
    return transferenciaID;
  }

  public Estado getEstadoDeTransferencia() {
    return estadoDeTransferencia;
  }
}
