package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.CrearWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CrearWalletUseCase extends UseCaseForCommand<CrearWallet> {

  private final UsuarioRepositorio usuarioRepositorio;

  public CrearWalletUseCase(UsuarioRepositorio usuarioRepositorio) {
    this.usuarioRepositorio = usuarioRepositorio;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<CrearWallet> crearWallet) {
    return crearWallet.flatMapMany(command -> {
      var usuario = usuarioRepositorio.obtenerDatosUsuario(command.getUsuarioID().value()).block();

      var wallet = new Wallet(command.getWalletID(), command.getUsuarioID(), command.getSaldo(),
          command.getMotivos());

      wallet.asignarUsuario(command.getUsuarioID(), usuario.getNombre(), usuario.getEmail(),
          usuario.getTelefono());

      return Flux.fromIterable(wallet.getUncommittedChanges());

    });
  }
}
