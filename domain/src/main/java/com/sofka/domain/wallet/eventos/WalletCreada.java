package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class WalletCreada extends DomainEvent {

  private final WalletID walletID;

  private final UsuarioID usuarioID;

  private final Saldo saldo;


  public WalletCreada(WalletID walletID, UsuarioID usuarioID, Saldo saldo) {
    super("com.sofka.domain.wallet.eventos.WalletCreada");
    this.walletID = walletID;
    this.usuarioID = usuarioID;
    this.saldo = saldo;
  }

  public WalletID getWalletID() {
    return walletID;
  }

  public UsuarioID getUsuarioID() {
    return usuarioID;
  }

  public Saldo getSaldo() {
    return saldo;
  }

}