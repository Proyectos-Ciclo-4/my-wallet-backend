package com.sofka.generic.materialize.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedHash {

  private String hash;

  private String typeName;

  private String walletId;

  public SavedHash(String hash, String typeName, String walletId) {
    this.hash = hash;
    this.typeName = typeName;
    this.walletId = walletId;
  }
}
