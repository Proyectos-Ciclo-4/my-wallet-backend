package com.sofka.business.usecase.gateway;

import reactor.core.publisher.Mono;

public interface UsuarioRepositorio {

  Mono<Boolean> obtenerDatosUsuario(String email, String telefono);

}
