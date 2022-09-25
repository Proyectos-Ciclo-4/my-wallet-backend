package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;

public class Nombre implements ValueObject<String> {

  private final String nombreDeUsuario;

  public Nombre(String nombreDeUsuario) {
    this.nombreDeUsuario = nombreDeUsuario;
  }

  @Override
  public String value() {
    return this.nombreDeUsuario;
  }

}
