package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class TransferenciaCreada extends DomainEvent {

  private final WalletID walletDestino;

  private final TransferenciaID transferenciaID;

  private final Estado estadoDeTransferencia;

  private final Cantidad valor;

  private final Motivo motivo;

  public TransferenciaCreada(WalletID walletDestino, TransferenciaID transferenciaID,
      Cantidad valor, Motivo motivo) {
    super("com.sofka.domain.wallet.eventos.TransferenciaCreada");
    this.walletDestino = walletDestino;
    this.transferenciaID = transferenciaID;
    this.estadoDeTransferencia = new Estado(TipoDeEstado.PENDIENTE);
    this.valor = valor;
    this.motivo = motivo;
  }

  public WalletID getWalletDestino() {
    return walletDestino;
  }

  public TransferenciaID getTransferenciaID() {
    return transferenciaID;
  }

  public Estado getEstadoDeTransferencia() {
    return estadoDeTransferencia;
  }

  public Cantidad getValor() {
    return valor;
  }

  public Motivo getMotivo() {
    return motivo;
  }
}
