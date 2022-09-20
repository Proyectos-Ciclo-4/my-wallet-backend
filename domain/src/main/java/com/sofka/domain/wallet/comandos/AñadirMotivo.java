package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class AñadirMotivo extends Command {

    private final WalletID walletID;

    private final Motivo descripcion;

    public AñadirMotivo(WalletID walletID, Motivo descripcion) {
        this.walletID = walletID;
        this.descripcion = descripcion;
    }

    public WalletID getWalletID() {
        return walletID;
    }

    public Motivo getDescripcion() {
        return descripcion;
    }
}
