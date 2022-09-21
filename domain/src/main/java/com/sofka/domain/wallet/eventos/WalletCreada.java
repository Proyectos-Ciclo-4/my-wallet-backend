package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

import java.util.List;

public class WalletCreada extends DomainEvent {

    private final WalletID walletID;

    private final UsuarioID usuarioID;

    private final Saldo saldo;

    private final List<Motivo> motivos;

    public WalletCreada(WalletID walletID, UsuarioID usuarioID, Saldo saldo, List<Motivo> motivos) {
        super("com.sofka.domain.wallet.WalletCreada");
        this.walletID = walletID;
        this.usuarioID = usuarioID;
        this.saldo = saldo;
        this.motivos = motivos;
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

    public List<Motivo> getMotivos() {
        return motivos;
    }
}
