package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AgregarContacto extends Command {

  private String nombre;

  private String telefono;

  private String email;

  private String contactoId;

  private String walletId;
}
