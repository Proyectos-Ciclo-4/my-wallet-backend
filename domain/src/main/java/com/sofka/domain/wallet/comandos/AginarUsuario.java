package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AginarUsuario extends Command {

  private UsuarioID usuarioID;

  private Nombre nombre;

  private Email email;

  private Telefono numero;

}
