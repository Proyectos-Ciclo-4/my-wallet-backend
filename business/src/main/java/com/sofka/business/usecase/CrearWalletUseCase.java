package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CrearWalletUseCase extends UseCaseForCommand<CrearWallet> {

  @Override
  public Flux<DomainEvent> apply(Mono<CrearWallet> crearWallet) {
    return crearWallet.flatMapMany(command -> {
      var wallet = new Wallet(command.getUsuarioID(), new Saldo(100.00));

      wallet.asignarUsuario(command.getUsuarioID(), new Nombre("nombre"), command.getEmail(),
          command.getTelefono());

      return Flux.fromIterable(wallet.getUncommittedChanges());
    });
  }



  /*{
    return crearWallet.flatMapMany(command -> usuarioRepositorio.obtenerDatosUsuario(command.getEmail().value(),
        command.getTelefono().value()).flatMapMany((usuarioData)-> {
          System.out.println("usuarioData = " + usuarioData);

          if (usuarioData != null) {
            var wallet = new Wallet(WalletID.of(""));
            wallet.rechazarCreacion(usuarioData.identity().value());
            wallet.rechazarCreacion(usuarioData.identity().value());

            return Flux.fromIterable(wallet.getUncommittedChanges());
          }

          var wallet = new Wallet(command.getUsuarioID(), new Saldo(100.00));

          wallet.asignarUsuario(command.getUsuarioID(), usuarioData.getNombre(),
              usuarioData.getEmail(),
              usuarioData.getTelefono());

          return Flux.fromIterable(wallet.getUncommittedChanges());
        }
    ));
  }*/
}