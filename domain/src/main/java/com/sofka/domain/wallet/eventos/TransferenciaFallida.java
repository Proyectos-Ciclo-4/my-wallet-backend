package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.Getter;

@Getter
public class TransferenciaFallida extends DomainEvent {

  private final WalletID walletOrigen;

  private final WalletID walletDestino;

  private final TransferenciaID transferenciaID;

  private final Estado estadoDeTransferencia;

  private final Cantidad valor;

  private final Motivo motivo;

  public TransferenciaFallida(WalletID walletOrigen, WalletID walletDestino,
      TransferenciaID transferenciaID,
      Estado estadoDeTransferencia, Cantidad valor, Motivo motivo) {
    super("com.sofka.domain.wallet.eventos.TransferenciaFallida");

    this.walletOrigen = walletOrigen;
    this.walletDestino = walletDestino;
    this.transferenciaID = transferenciaID;
    this.estadoDeTransferencia = estadoDeTransferencia;
    this.valor = valor;
    this.motivo = motivo;
  }
}