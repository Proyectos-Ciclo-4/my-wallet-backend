package com.sofka.generic.materialize.model;

import java.util.List;
import lombok.Data;

@Data
public class WalletModel {

  private String walletId;

  private String usuario;

  private List<String> motivos;

  private Double saldo;

  private List<TransaccionDeHistorial> historial;
}