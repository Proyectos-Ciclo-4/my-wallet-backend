package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;

public class AginarUsuario extends Command {

  private final UsuarioID usuarioID;

  private final Nombre nombre;

  private final Email email;

  private final Telefono numero;

  public AginarUsuario(UsuarioID usuarioID, Nombre nombre, Email email, Telefono numero) {
    this.usuarioID = usuarioID;
    this.nombre = nombre;
    this.email = email;
    this.numero = numero;
  }

  public UsuarioID getUsuarioID() {
    return usuarioID;
  }

  public Nombre getNombre() {
    return nombre;
  }

  public Email getEmail() {
    return email;
  }

  public Telefono getNumero() {
    return numero;
  }
}
