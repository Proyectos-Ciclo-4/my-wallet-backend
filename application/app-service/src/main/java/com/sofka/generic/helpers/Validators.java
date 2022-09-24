package com.sofka.generic.helpers;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Validators {

  private final UsuarioRepositorio usuarioRepositorio;

  public Validators(UsuarioRepositorio usuarioRepositorio) {
    this.usuarioRepositorio = usuarioRepositorio;
  }

  public Mono<CrearWallet> validateUser(Mono<CrearWallet> command) {
    return command.doOnNext(
        crearWallet -> usuarioRepositorio.obtenerDatosUsuario(crearWallet.getEmail().value(),
            crearWallet.getTelefono().value()).subscribe(usuarioData -> {
          if (usuarioData != null) {
            var wallet = new Wallet(WalletID.of(""));
            wallet.rechazarCreacion(usuarioData.identity().value());
          }
        }));
  }
}