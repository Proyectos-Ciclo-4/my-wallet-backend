package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MotivoCreado extends DomainEvent {

  private final Motivo motivo;

  public MotivoCreado(Motivo motivo) {
    super("com.sofka.domain.wallet.eventos.MotivoCreado");
    this.motivo = motivo;
  }
}