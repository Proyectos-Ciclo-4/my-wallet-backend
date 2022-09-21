package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.Entity;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;

public class Usuario extends Entity<UsuarioID> {

  private Nombre nombre;

  private Email email;

  private Telefono telefono;

  public Usuario(UsuarioID entityId, Nombre nombre, Email email, Telefono telefono) {
    super(entityId);
    this.nombre = nombre;
    this.email = email;
    this.telefono = telefono;
  }

  public Usuario(UsuarioID entityId) {
    super(entityId);
  }

  public Nombre getNombre() {
    return nombre;
  }

  public void setNombre(Nombre nombre) {
    this.nombre = nombre;
  }

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email email) {
    this.email = email;
  }

  public Telefono getTelefono() {
    return telefono;
  }

  public void setTelefono(Telefono telefono) {
    this.telefono = telefono;
  }
}
