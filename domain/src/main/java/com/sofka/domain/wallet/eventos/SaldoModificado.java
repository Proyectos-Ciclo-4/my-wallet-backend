package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class SaldoModificado extends DomainEvent {

    private final WalletID walletID;

    private final Saldo saldo;

    public SaldoModificado(WalletID walletID, Saldo saldo) {
        super("com.sofka.domain.wallet.SaldoModificado");
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
