package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransferenciaCreada extends DomainEvent {

  private final WalletID walletOrigen;

  private final WalletID walletDestino;

  private final TransferenciaID transferenciaID;

  private final Estado estadoDeTransferencia;

  private final Cantidad valor;

  private final Motivo motivo;

  public TransferenciaCreada(WalletID walletOrigen, WalletID walletDestino, Cantidad valor,
      Motivo motivo) {
    super("com.sofka.domain.wallet.eventos.TransferenciaCreada");
    this.walletOrigen = walletOrigen;
    this.walletDestino = walletDestino;
    this.transferenciaID = new TransferenciaID();
    this.estadoDeTransferencia = new Estado(TipoDeEstado.PENDIENTE);
    this.valor = valor;
    this.motivo = motivo;
  }
}
