package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.Identity;

public class UsuarioID extends Identity {

  public UsuarioID() {
  }

  public UsuarioID(String id) {
    super(id);
  }

  public static UsuarioID of(String id) {
    return new UsuarioID(id);
  }
}

