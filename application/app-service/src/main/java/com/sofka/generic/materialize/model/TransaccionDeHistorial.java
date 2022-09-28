package com.sofka.generic.materialize.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class TransaccionDeHistorial {

  private String walletId;

  private String fecha;

  private String estado;

  private Motivo motivo;

  private Double valor;

  private String destino;

  private String transferencia_id;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Motivo {

    private String descripcion;

    private String color;

  }

}