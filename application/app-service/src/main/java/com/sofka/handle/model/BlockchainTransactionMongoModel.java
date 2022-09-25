package com.sofka.handle.model;

public class BlockchainTransactionMongoModel {

  private String walletID;

  private String blockchainTransactionID;

  public BlockchainTransactionMongoModel(String walletID, String blockchainTransactionID) {
    this.walletID = walletID;
    this.blockchainTransactionID = blockchainTransactionID;
  }

  public BlockchainTransactionMongoModel() {
  }

  public String getWalletID() {
    return walletID;
  }

  public void setWalletID(String walletID) {
    this.walletID = walletID;
  }

  public String getBlockchainTransactionID() {
    return blockchainTransactionID;
  }

  public void setBlockchainTransactionID(String blockchainTransactionID) {
    this.blockchainTransactionID = blockchainTransactionID;
  }
}
