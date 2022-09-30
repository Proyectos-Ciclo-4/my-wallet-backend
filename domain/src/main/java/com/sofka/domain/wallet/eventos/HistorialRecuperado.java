package com.sofka.domain.wallet.eventos;

public class HistorialRecuperado extends TransferenciaExitosa {

  public HistorialRecuperado(TransferenciaExitosa transferenciaExitosa) {
    super(transferenciaExitosa.getWalletOrigen(), transferenciaExitosa.getWalletDestino(),
        transferenciaExitosa.getTransferenciaID(), transferenciaExitosa.getValor(),
        transferenciaExitosa.getMotivo(), transferenciaExitosa.getEstado(), "HistorialRecuperado");
  }
}
