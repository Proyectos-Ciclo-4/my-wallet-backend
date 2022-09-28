package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.Identity;
import lombok.ToString;

@ToString
public class TransferenciaID extends Identity {

  public TransferenciaID() {
    super();
  }

  public TransferenciaID(String id) {
    super(id);
  }

  public static TransferenciaID of(String id) {
    return new TransferenciaID(id);
  }
}

