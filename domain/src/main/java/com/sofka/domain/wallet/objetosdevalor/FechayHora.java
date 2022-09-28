package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;
import java.time.LocalDate;
import lombok.ToString;

@ToString
public class FechayHora implements ValueObject<LocalDate> {

  private final LocalDate fecha;

  public FechayHora(LocalDate fechayhora) {
    this.fecha = fechayhora;
  }

  @Override
  public LocalDate value() {
    return this.fecha;
  }
}
