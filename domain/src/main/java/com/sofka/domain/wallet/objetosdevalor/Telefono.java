package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;
import lombok.ToString;

@ToString
public class Telefono implements ValueObject<String> {

  private final String numero;

  public Telefono(String numero) {
    this.numero = numero;
  }

  @Override
  public String value() {
    return this.numero;
  }

}
