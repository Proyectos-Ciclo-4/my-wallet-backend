package com.sofka.generic.materialize.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedHash {

  private String hash;

  private String typeName;

  public SavedHash(String hash, String typeName) {
    this.hash = hash;
    this.typeName = typeName;
  }
}
