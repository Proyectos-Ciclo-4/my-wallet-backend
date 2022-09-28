package com.sofka.domain.wallet.objetosdevalor;

import co.com.sofka.domain.generic.ValueObject;
import lombok.ToString;

@ToString
public class Motivo implements ValueObject<Motivo.Props> {

  private final String descripcion;

  private final String color;

  public Motivo(String descripcion, String color) {

    validarCampos(descripcion, color);

    this.descripcion = descripcion;
    this.color = color;
  }

  private static void validarCampos(String descripcion, String color) {
    if (color.charAt(0) != '#') {
      throw new IllegalArgumentException("El color debe empezar con #");
    }

    if (descripcion.isBlank()) {
      throw new IllegalArgumentException("La descripcion no puede estar vacia");
    }
  }

  @Override
  public Props value() {
    return new Props() {
      @Override
      public String descripcion() {
        return descripcion;
      }

      @Override
      public String color() {
        return color;
      }
    };
  }

  public interface Props {

    String descripcion();

    String color();
  }
}