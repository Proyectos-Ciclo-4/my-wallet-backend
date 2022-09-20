package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class EliminarContacto extends Command {

  private final WalletID walletID;
  private final UsuarioID contactoID;

  public WalletID getWalletID() {
    return walletID;
  }

  public UsuarioID getContactoID() {
    return contactoID;
  }

  public EliminarContacto(WalletID walletID, UsuarioID contactoID) {
    this.walletID = walletID;
    this.contactoID = contactoID;
  }
}
