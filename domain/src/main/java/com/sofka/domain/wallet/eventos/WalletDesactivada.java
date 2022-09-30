package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;

public class WalletDesactivada extends DomainEvent {

  public WalletDesactivada() {
    super("com.sofka.domain.wallet.eventos.WalletDesactivada");
  }
}
