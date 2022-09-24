package com.sofka.adapters.repositories;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Usuario;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepository implements UsuarioRepositorio {

  private final EventStoreRepository repository;

  public UserRepository(EventStoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Usuario> obtenerDatosUsuario(String email, String telefono) {
    /*var usuarioId = UsuarioID.of("123");
    var nombre = new Nombre("Juan");
    var emaill = new Email("daniel@gmail.com");
    var telefonoo = new Telefono("123456789");

    return Mono.just(new Usuario(usuarioId, nombre, emaill, telefonoo));*/
    return repository.getUser(email, telefono);
  }
}
