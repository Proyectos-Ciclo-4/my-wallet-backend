package com.sofka.generic.materialize.model;

import lombok.Data;


@Data
public class TransaccionDeHistorial {

  private String walletId;

  private String fecha;

  private String estado;

  private String motivo;

  private Double valor;

  private String destino;

  private String transferencia_id;

}