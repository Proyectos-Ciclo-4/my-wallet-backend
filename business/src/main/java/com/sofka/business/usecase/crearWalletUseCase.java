package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class crearWalletUseCase extends UseCaseForCommand<CrearWallet> {


  @Override
  public Flux<DomainEvent> apply(Mono<CrearWallet> crearWallet) {
    return crearWallet.flatMapMany(command -> {

      var wallet = new Wallet(command.getWalletID(), command.getUsuarioID(), command.getSaldo(),
          command.getMotivos());
      // wallet.asignarUsuario();
      return Flux.fromIterable(wallet.getUncommittedChanges());

    });
  }
}
