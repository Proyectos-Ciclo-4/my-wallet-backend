package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;

public class Email implements ValueObject<String> {

    private final String direccion;

    public Email(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String value(){return this.direccion;}

}
