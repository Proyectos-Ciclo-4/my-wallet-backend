package com.sofka.handle.model;

public class BlockchainTransactionMongoModel {

  private String usuarioID;

  private String blockchainTransactionID;

  public BlockchainTransactionMongoModel(String usuarioID, String blockchainTransactionID) {
    this.usuarioID = usuarioID;
    this.blockchainTransactionID = blockchainTransactionID;
  }

  public BlockchainTransactionMongoModel(){
  }

  public String getUsuarioID() {
    return usuarioID;
  }

  public void setUsuarioID(String usuarioID) {
    this.usuarioID = usuarioID;
  }

  public String getBlockchainTransactionID() {
    return blockchainTransactionID;
  }

  public void setBlockchainTransactionID(String blockchainTransactionID) {
    this.blockchainTransactionID = blockchainTransactionID;
  }
}
