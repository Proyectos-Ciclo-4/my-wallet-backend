package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.Entity;
import com.sofka.domain.wallet.objetosdevalor.BlockchainID;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.FechayHora;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class Transferencia extends Entity<TransferenciaID> {

  private WalletID walletDestino;
  private BlockchainID blockchainID;
  private Estado estado;
  private FechayHora fechayHora;
  private Cantidad cantidad;
  private Motivo motivo;

  public Transferencia(TransferenciaID entityId, WalletID walletDestino, BlockchainID blockchainID,
      Estado estado, FechayHora fechayHora, Cantidad cantidad, Motivo motivo) {
    super(entityId);
    this.walletDestino = walletDestino;
    this.blockchainID = blockchainID;
    this.estado = estado;
    this.fechayHora = fechayHora;
    this.cantidad = cantidad;
    this.motivo = motivo;
  }

  public WalletID getWalletDestino() {
    return walletDestino;
  }

  public void setWalletDestino(WalletID walletDestino) {
    this.walletDestino = walletDestino;
  }

  public BlockchainID getBlockchainID() {
    return blockchainID;
  }

  public void setBlockchainID(BlockchainID blockchainID) {
    this.blockchainID = blockchainID;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public FechayHora getFechayHora() {
    return fechayHora;
  }

  public void setFechayHora(FechayHora fechayHora) {
    this.fechayHora = fechayHora;
  }

  public Cantidad getCantidad() {
    return cantidad;
  }

  public void setCantidad(Cantidad cantidad) {
    this.cantidad = cantidad;
  }

  public Motivo getMotivo() {
    return motivo;
  }

  public void setMotivo(Motivo motivo) {
    this.motivo = motivo;
  }
}
