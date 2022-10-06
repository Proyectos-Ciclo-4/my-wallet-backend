package com.sofka.adapters.repositories;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.generic.materialize.model.UserModel;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository implements UsuarioRepositorio {

  private final EventStoreRepository repository;

  public UserRepository(EventStoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Boolean> obtenerDatosUsuario(String email, String telefono) {
    return repository.userExists(email, telefono);
  }

  public Flux<UserModel> datos(String telefono) {
    return repository.getUserByPhoneNumber(telefono);
  }
}