package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;

public class TransferenciaExitosa extends DomainEvent {

    public TransferenciaExitosa(){
        super("com.sofka.domain.wallet.TransferenciaExitosa");
    }
}
