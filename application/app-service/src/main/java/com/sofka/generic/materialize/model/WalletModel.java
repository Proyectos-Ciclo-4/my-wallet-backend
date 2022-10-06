package com.sofka.generic.materialize.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class WalletModel {

  private String walletId;

  private String usuario;

  private List<Motivo> motivos;

  private List<Contacto> contactos;

  private Double saldo;

  private List<TransaccionDeHistorial> historial;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Motivo {

    private String descripcion;

    private String color;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Contacto {

    private String walletId;

    private String nombre;

    private String telefono;

    private String email;

  }
}