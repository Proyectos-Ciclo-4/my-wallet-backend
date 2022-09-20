package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;

public class Estado implements ValueObject<Estado.TipoDeEstado> {

    private final TipoDeEstado tipoDeEstado;

    public Estado(TipoDeEstado tipoDeEstado) {
        this.tipoDeEstado = tipoDeEstado;
    }

    @Override
    public TipoDeEstado value(){return this.tipoDeEstado;}

    public enum TipoDeEstado{
        PENDIENTE,RECHAZADA,EXITOSA
    }

}
