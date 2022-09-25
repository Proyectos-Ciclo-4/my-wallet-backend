package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.Identity;

public class WalletID extends Identity {

  public WalletID() {
    super();
  }

  public WalletID(String id) {
    super(id);
  }

  public static WalletID of(String id) {
    return new WalletID(id);
  }
}

