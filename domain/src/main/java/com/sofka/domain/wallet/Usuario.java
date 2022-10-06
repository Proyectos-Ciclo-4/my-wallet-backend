package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.Entity;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Usuario extends Entity<UsuarioID> {

  private Nombre nombre;

  private Email email;

  private Telefono telefono;

  public Usuario() {
    super(UsuarioID.of("0"));
  }

  public Usuario(UsuarioID entityId, String nombre, String email, String telefono) {
    super(entityId);
    this.nombre = new Nombre(nombre);
    this.email = new Email(email);
    this.telefono = new Telefono(telefono);
  }
}
