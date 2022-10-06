package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EliminarContacto extends Command {

  private String walletId;

  private String contactoId;
}
