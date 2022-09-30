package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;
import lombok.ToString;

@ToString
public class Cantidad implements ValueObject<Double> {

  private final Double monto;

  public Cantidad(Double monto) {
    this.monto = monto;
  }

  @Override
  public Double value() {
    return this.monto;
  }

  public Cantidad negate() {
    return new Cantidad(-this.monto);
  }

}
