package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.awt.font.NumericShaper;

public class ContactoAnadido extends DomainEvent {
  private final Nombre nombre;
  private final Email email;
  private final Telefono telefono;

  public ContactoAnadido(Nombre nombre, Email email,
      Telefono telefono) {
    super("com.sofka.domain.wallet.eventos.ContactoAnadido");
    this.nombre = nombre;
    this.email = email;
    this.telefono = telefono;
  }

  public Nombre getNombre() {
    return nombre;
  }

  public Email getEmail() {
    return email;
  }

  public Telefono getTelefono() {
    return telefono;
  }
}
