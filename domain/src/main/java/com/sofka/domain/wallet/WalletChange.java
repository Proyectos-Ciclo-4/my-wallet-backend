package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.EventChange;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;

import java.util.ArrayList;

public class WalletChange extends EventChange {

    public WalletChange(Wallet wallet){

        apply((WalletCreada event)->{
            wallet.saldo = event.getSaldo();
            wallet.motivos = new ArrayList<Motivo>();
        });
    }
}
