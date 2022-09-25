package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.Identity;

public class TransferenciaID extends Identity {

  public TransferenciaID() {
  }

  public TransferenciaID(String id) {
    super(id);
  }

  public static TransferenciaID of(String id) {
    return new TransferenciaID(id);
  }
}

