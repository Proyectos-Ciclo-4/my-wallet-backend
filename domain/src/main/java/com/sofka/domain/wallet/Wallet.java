package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;

import java.util.List;
import java.util.Optional;

public class Wallet extends AggregateEvent<WalletID> {

    protected UsuarioID usuarioID;

    protected Saldo saldo;

    protected List<Motivo> motivos;

    protected Usuario usuario;

    protected List<Transferencia> transferencias;


    public Wallet(WalletID entityId, UsuarioID usuarioID, Saldo saldo, List<Motivo> motivos) {
        super(entityId);
        subscribe(new WalletChange(this));
        appendChange(new WalletCreada(entityId,usuarioID,saldo,motivos)).apply();
    }

    private Wallet(WalletID entityId){
        super(entityId);
        subscribe(new WalletChange(this));
    }

    public Wallet from(WalletID walletID, List<DomainEvent> events){
        Wallet wallet = new Wallet(walletID);
        events.forEach(event -> {wallet.applyEvent(event);});
        return wallet;
    }

    public Optional<Transferencia> getTransferenciaPorId(TransferenciaID transferenciaID){
        return transferencias.stream().filter((transferencia -> transferencia.identity().equals(transferenciaID))).findFirst();
    }



}
