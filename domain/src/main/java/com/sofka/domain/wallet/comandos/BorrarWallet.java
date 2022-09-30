package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import lombok.Data;

@Data
public class BorrarWallet extends Command {

  private String walletId;

  public BorrarWallet(String walletId) {
    this.walletId = walletId;
  }
}