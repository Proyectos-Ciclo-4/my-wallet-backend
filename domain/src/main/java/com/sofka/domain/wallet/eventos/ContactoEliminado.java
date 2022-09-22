package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class ContactoEliminado extends DomainEvent {

  private final WalletID walletID;
  private final UsuarioID contactoID;

  public ContactoEliminado(WalletID walletID, UsuarioID contactoID) {
    super("com.sofka.domain.wallet.eventos.ContactoEliminado");
    this.walletID = walletID;
    this.contactoID = contactoID;
  }

  public WalletID getWalletID() {
    return walletID;
  }

  public UsuarioID getContactoID() {
    return contactoID;
  }
}
