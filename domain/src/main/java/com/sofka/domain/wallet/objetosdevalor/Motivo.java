package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;

public class Motivo implements ValueObject<String> {

    private final String descripcion;

    public Motivo(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String value(){return this.descripcion;}

}
