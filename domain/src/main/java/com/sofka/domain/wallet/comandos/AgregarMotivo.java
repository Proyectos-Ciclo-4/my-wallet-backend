package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregarMotivo extends Command {

  private String walletID;

  private String descripcion;

  private String color;

}
