package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;

public class UsuarioExistente extends DomainEvent {

  private UsuarioID usuarioID;

  public UsuarioExistente(String usuarioId) {
    super("com.sofka.domain.wallet.eventos.UsuarioExistente");

    this.usuarioID = new UsuarioID(usuarioId);
  }
}
