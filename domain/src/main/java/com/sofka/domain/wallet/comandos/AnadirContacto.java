package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class AnadirContacto extends Command {

    private final WalletID walletID;
    private final Usuario contacto;

    public WalletID getWalletID() {
        return walletID;
    }

    public Usuario getContacto() {
        return contacto;
    }

    public AnadirContacto(WalletID walletID, Usuario contacto) {
        this.walletID = walletID;
        this.contacto = contacto;
    }
}
