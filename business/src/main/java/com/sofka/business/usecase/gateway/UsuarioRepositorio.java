package com.sofka.business.usecase.gateway;

import com.sofka.domain.wallet.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepositorio {

  Mono<Usuario> obtenerDatosUsuario(String id);

}
