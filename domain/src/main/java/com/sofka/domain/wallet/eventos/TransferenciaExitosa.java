package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransferenciaExitosa extends DomainEvent {

  private final WalletID walletOrigen;

  private final WalletID walletDestino;

  private final TransferenciaID transferenciaID;

  private final Cantidad valor;

  private final Motivo motivo;

  private final Estado estado;


  public TransferenciaExitosa(WalletID walletOrigen, WalletID walletDestino,
      TransferenciaID transferenciaID, Cantidad valor, Motivo motivo,
      Estado estado) {
    super("com.sofka.domain.wallet.eventos.TransferenciaExitosa");
    this.walletOrigen = walletOrigen;
    this.walletDestino = walletDestino;
    this.transferenciaID = transferenciaID;
    this.valor = valor;
    this.motivo = motivo;
    this.estado = estado;
  }
}