package com.sofka.generic.materialize.model;

import java.util.List;
import lombok.Data;

@Data
public class WalletModel {

  private String walletId;

  private String usuario;

  private List<String> motivos;

  private List<String> contactos;

  private List<String> ultimasTransacciones;

  private Double saldo;

}
