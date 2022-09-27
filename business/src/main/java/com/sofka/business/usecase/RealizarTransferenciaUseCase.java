package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RealizarTransferenciaUseCase extends UseCaseForCommand<RealizarTransferencia> {

  private final WalletDomainEventRepository walletsRepository;

  public RealizarTransferenciaUseCase(WalletDomainEventRepository walletsRepository) {
    this.walletsRepository = walletsRepository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<RealizarTransferencia> realizarTransferenciaMono) {
    return realizarTransferenciaMono.flatMapMany(
        realizarTransferencia -> walletsRepository.obtenerEventos(
            realizarTransferencia.getWalletDestino().value()).collectList().flatMapMany(
            walletDestinoEvents -> walletsRepository.obtenerEventos(
                    realizarTransferencia.getWalletOrigen().value()).collectList()
                .flatMapMany(walletPropiaEvents -> {
                  var walletDestinoid = realizarTransferencia.getWalletDestino();
                  var walletPropiaId = realizarTransferencia.getWalletOrigen();

                  var walletPropia = Wallet.from(walletPropiaId, walletPropiaEvents);
                  var walletDestino = Wallet.from(walletDestinoid, walletDestinoEvents);

                  var walletOrigen = realizarTransferencia.getWalletOrigen();
                  var walletDestinoID = realizarTransferencia.getWalletDestino();
                  var cantidad = realizarTransferencia.getValor();
                  var motivo = realizarTransferencia.getMotivo();

                  walletPropia.crearTransferencia(walletOrigen, walletDestinoID,
                      cantidad, motivo);

                  walletDestino.crearTransferencia(walletOrigen, walletDestinoID,
                      cantidad, motivo);

                  var cambiosPropia = walletPropia.getUncommittedChanges();
                  var cambiosDestino = walletDestino.getUncommittedChanges();

                  var cambiosPropios = Flux.fromIterable(cambiosPropia);
                  var cambiosDeDestino = Flux.fromIterable(cambiosDestino);

                  return Flux.concat(cambiosPropios, cambiosDeDestino);
                })));
  }
}