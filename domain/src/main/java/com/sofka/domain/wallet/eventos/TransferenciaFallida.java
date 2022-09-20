package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;

public class TransferenciaFallida extends DomainEvent {

    public TransferenciaFallida() {
        super("com.sofka.domain.wallet.TransferenciaFallida");
    }
}
