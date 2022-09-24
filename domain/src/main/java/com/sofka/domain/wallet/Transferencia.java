package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.Entity;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.FechayHora;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class Transferencia extends Entity<TransferenciaID> {

  private WalletID walletDestino;
  private Estado estado;
  private FechayHora fechayHora;
  private Cantidad cantidad;
  private Motivo motivo;

  public Transferencia(TransferenciaID entityId, WalletID walletDestino, Estado estado,
      FechayHora fechayHora, Cantidad cantidad, Motivo motivo) {
    super(entityId);
    this.walletDestino = walletDestino;
    this.estado = estado;
    this.fechayHora = fechayHora;
    this.cantidad = cantidad;
    this.motivo = motivo;
  }

  public Estado getEstado() {
    return estado;
  }

  public FechayHora getFechayHora() {
    return fechayHora;
  }

  public Cantidad getCantidad() {
    return cantidad;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public void setFechayHora(FechayHora fechayHora) {
    this.fechayHora = fechayHora;
  }

  public void setCantidad(Cantidad cantidad) {
    this.cantidad = cantidad;
  }

  public Motivo getMotivo() {
    return motivo;
  }

  public WalletID getWalletDestino() {
    return walletDestino;
  }
}
