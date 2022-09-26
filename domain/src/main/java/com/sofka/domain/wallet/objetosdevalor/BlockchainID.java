package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;

public class BlockchainID implements ValueObject<String> {

  private final String BlockainID;

  public BlockchainID(String BlockainID) {
    this.BlockainID = BlockainID;
  }

  @Override
  public String value() {
    return this.BlockainID;
  }

}
