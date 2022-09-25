package com.sofka.generic.helpers;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Validators {

  private final UsuarioRepositorio usuarioRepositorio;

  private final WalletDomainEventRepository walletsRepository;

  public Validators(UsuarioRepositorio usuarioRepositorio,
      WalletDomainEventRepository walletsRepository) {
    this.usuarioRepositorio = usuarioRepositorio;
    this.walletsRepository = walletsRepository;
  }

  public Mono<CrearWallet> validateUser(Mono<CrearWallet> command) {
    return command.flatMap(
        crearWallet -> {
          log.info("Validating user {}", crearWallet);

          return usuarioRepositorio.obtenerDatosUsuario(crearWallet.getEmail().value(),
              crearWallet.getTelefono().value()).flatMap(usuarioData -> {

            if (usuarioData) {
              log.error("User already exists");
              return Mono.error(new RuntimeException("El usuario ya existe"));
            }

            return Mono.just(crearWallet);
          });
        });
  }

  public Mono<RealizarTransferencia> validateWallet(Mono<RealizarTransferencia> body) {
    return body.flatMap(realizarTransferencia -> {
      var walletDestino = realizarTransferencia.getWalletDestino().value();

      return walletsRepository.exists(walletDestino).flatMap(exists -> {

        if (!exists) {
          log.error("Wallet destino no existe");

          return Mono.error(new RuntimeException("Wallet destino no existe"));
        }

        return Mono.just(realizarTransferencia);
      });
    });
  }
}