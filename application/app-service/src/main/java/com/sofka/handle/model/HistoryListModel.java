package com.sofka.generic.handle.model;

import java.util.List;
import lombok.Data;

@Data
public class HistoryListModel {

  private String walletId;

  private String saldo;

  private Usuario usuario;

  private List<Motivo> motivos;

  @Data
  public static class Usuario {

    private String nombre;

    private String email;

    private String telefono;
  }

  @Data
  public static class Motivo {

    private String description;
  }
}