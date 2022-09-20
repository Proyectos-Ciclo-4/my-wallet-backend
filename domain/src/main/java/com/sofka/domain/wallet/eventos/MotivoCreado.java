package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class MotivoCreado extends DomainEvent {

    private final WalletID walletID;

    private final Motivo descripcion;

    public MotivoCreado(WalletID walletID, Motivo descripcion) {
        super("com.sofka.domain.wallet.MotivoCreado");
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
