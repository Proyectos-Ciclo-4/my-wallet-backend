package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

public class CrearTransferencia extends Command {

    private final WalletID walletID;

    private final TransferenciaID transferenciaID;

    private final Estado estadoDeTransferencia;

    private final Cantidad valor;

    private final Motivo motivo;

    public CrearTransferencia(WalletID walletID, TransferenciaID transferenciaID, Estado estadoDeTransferencia, Cantidad valor, Motivo motivo) {
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
