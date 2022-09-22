package com.sofka.adapters.repositories;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepository implements UsuarioRepositorio {

  @Override
  public Mono<Usuario> obtenerDatosUsuario(String id) {
    var usuarioId = UsuarioID.of("123");
    var nombre = new Nombre("Juan");
    var email = new Email("daniel@gmail.com");
    var telefono = new Telefono("123456789");

    return Mono.just(new Usuario(usuarioId, nombre, email, telefono));
  }
}
