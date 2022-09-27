package com.sofka.generic.helpers;

import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
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

  public Mono<RealizarTransferencia> validateWallet(ServerRequest request) {

    var bodyMono = request.bodyToMono(Object.class).flatMap(body -> {
      var requestMap = (Map<String, Object>) body;

      var walletDestino = requestMap.get("walletDestino").toString();
      var walletOrigen = requestMap.get("walletOrigen").toString();
      var valor = (Integer) requestMap.get("valor");
      var motivo = requestMap.get("motivo").toString();

      return Mono.just(
          new RealizarTransferencia(WalletID.of(walletOrigen), WalletID.of(walletDestino),
              new Cantidad(valor + 0.0), new Motivo(motivo)));
    });

    return bodyMono.flatMap(realizarTransferencia -> {

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