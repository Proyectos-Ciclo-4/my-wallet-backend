package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.EventChange;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;

import com.sofka.domain.wallet.objetosdevalor.Saldo;
import java.util.ArrayList;

public class WalletChange extends EventChange {

    public WalletChange(Wallet wallet) {

        apply((WalletCreada event) -> {
            wallet.saldo = new Saldo(100.00);
            wallet.motivos = new ArrayList<Motivo>();
            wallet.contactos = new ArrayList<Usuario>();
        });


    }
}
