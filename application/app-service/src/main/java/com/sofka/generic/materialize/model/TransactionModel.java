package com.sofka.generic.materialize.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionModel {

  private String transferenciaId;

  private String walletOrigenId;

  private String walletDestinoId;

  private Double valor;

  private String motivo;

  private LocalDateTime fecha;

}
