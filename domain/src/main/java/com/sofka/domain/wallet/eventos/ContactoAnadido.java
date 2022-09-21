package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class ContactoAnadido extends DomainEvent {

    private final WalletID walletID;
    private final Usuario contacto;

    public ContactoAnadido( WalletID walletID, Usuario contacto) {
        super("com.sofka.domain.wallet.ContactoAnadido");
        this.walletID = walletID;
        this.contacto = contacto;
    }

    public WalletID getWalletID() {
        return walletID;
    }

    public Usuario getContacto() {
        return contacto;
    }
}
