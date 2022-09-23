package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
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
      var usuario = usuarioRepositorio.obtenerDatosUsuario(command.getEmail().value(),
          command.getTelefono().value()).block();

      if (usuario == null) {
        var wallet = new Wallet(WalletID.of(""));
        wallet.rechazarCreacion(command.getUsuarioID().value());

        return Flux.fromIterable(wallet.getUncommittedChanges());
      }

      var wallet = new Wallet(command.getUsuarioID(), new Saldo(100.00));

      wallet.asignarUsuario(command.getUsuarioID(), usuario.getNombre(), usuario.getEmail(),
          usuario.getTelefono());

      return Flux.fromIterable(wallet.getUncommittedChanges());
    });
  }
}