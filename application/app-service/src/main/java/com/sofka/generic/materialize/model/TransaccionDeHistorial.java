package com.sofka.generic.materialize.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.DateOperators.Timezone;

@Data
public class TransaccionDeHistorial {

  private String walletId;

  private String fecha;

  private String estado;

  private String motivo;

  private Double valor;

  private String destino;

  private String transferencia_id;

  public TransaccionDeHistorial(String walletId, String destino) {
    this.transferencia_id = Double.toString(Math.random()*1000000);
    this.walletId = walletId;
    this.destino = destino;
    this.valor = 100.0;
    this.motivo = "Bienvenido(a) My Wallet!";
    this.estado = "EXITOSA";
    this.fecha = LocalDateTime.now().toString();
  }
}
