package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class ModificarSaldo extends Command {

    private final WalletID walletID;

    private final Saldo saldo;

    public ModificarSaldo(WalletID walletID, Saldo saldo) {
        this.walletID = walletID;
        this.saldo = saldo;
    }

    public WalletID getWalletID() {
        return walletID;
    }

    public Saldo getSaldo() {
        return saldo;
    }
}
