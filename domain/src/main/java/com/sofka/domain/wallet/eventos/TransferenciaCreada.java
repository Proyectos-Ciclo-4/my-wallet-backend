package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class TransferenciaCreada extends DomainEvent {

    private final WalletID walletID;

    private final TransferenciaID transferenciaID;

    private final Estado estadoDeTransferencia;

    private final Cantidad valor;

    private final Motivo motivo;

    public TransferenciaCreada(WalletID walletID, TransferenciaID transferenciaID, Estado estadoDeTransferencia, Cantidad valor, Motivo motivo) {
        super("com.sofka.domain.wallet.TransferenciaCreada");
        this.walletID = walletID;
        this.transferenciaID = transferenciaID;
        this.estadoDeTransferencia = estadoDeTransferencia;
        this.valor = valor;
        this.motivo = motivo;
    }

    public WalletID getWalletID() {
        return walletID;
    }

    public TransferenciaID getTransferenciaID() {
        return transferenciaID;
    }

    public Estado getEstadoDeTransferencia() {
        return estadoDeTransferencia;
    }

    public Cantidad getValor() {
        return valor;
    }

    public Motivo getMotivo() {
        return motivo;
    }
}
