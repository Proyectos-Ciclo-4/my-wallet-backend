package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Motivo;

public class MotivoCreado extends DomainEvent {

  private final Motivo descripcion;

  public MotivoCreado(Motivo descripcion) {
    super("com.sofka.domain.wallet.MotivoCreado");
    this.descripcion = descripcion;
  }

  public Motivo getDescripcion() {
    return descripcion;
  }
}
