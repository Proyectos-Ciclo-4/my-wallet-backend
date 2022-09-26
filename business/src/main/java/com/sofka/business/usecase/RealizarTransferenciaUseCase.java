package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
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
                realizarTransferencia.getWalletOrigen().value()).collectList()
            .flatMapMany(origenEvents -> {
               return walletsRepository.obtenerEventos(realizarTransferencia.getWalletDestino().value())
                  .collectList().flatMapMany(destinoEvents -> {

                     var walletDestinoid = realizarTransferencia.getWalletDestino();
                     var walletPropiaId = realizarTransferencia.getWalletOrigen();

                     System.out.println(walletPropiaId);

                     System.out.println("Eventos registrados de la wallet propia:");
                     origenEvents.forEach(event -> System.out.println(event.toString()));
                     var walletPropia = Wallet.from(walletPropiaId, origenEvents);
                     System.out.println("Eventos registrados de la wallet destino:");
                     destinoEvents.forEach(event -> System.out.println(event.toString()));
                     var walletDestino = Wallet.from(walletDestinoid, destinoEvents);
                     var cantidad = realizarTransferencia.getValor();
                     var motivo = realizarTransferencia.getMotivo();

                     walletPropia.crearTransferencia(walletDestinoid, new TransferenciaID(), cantidad,
                         motivo);

                     walletDestino.crearTransferencia(walletDestinoid, new TransferenciaID(), cantidad,
                         motivo);

                     var cambiosPropios = Flux.fromIterable(walletPropia.getUncommittedChanges());
                     var cambiosDeDestino = Flux.fromIterable(walletDestino.getUncommittedChanges());

                     return Flux.concat(cambiosPropios, cambiosDeDestino);
                  });
            }));
  }
}

  /*

  @Override
  public Flux<DomainEvent> apply(Mono<RealizarTransferencia> realizarTransferenciaMono) {
    return realizarTransferenciaMono.flatMapMany(
        realizarTransferencia -> walletsRepository.obtenerEventos(
                realizarTransferencia.getWalletOrigen().value()).collectList()
            .flatMapMany(origenEvents -> {

              walletsRepository.obtenerEventos(realizarTransferencia.getWalletDestino().value())
                  .collectList().flatMap(destinoEvents -> {

                    var walletDestinoid = realizarTransferencia.getWalletDestino();
                    var walletPropiaId = realizarTransferencia.getWalletOrigen();

                    System.out.println(walletPropiaId);

                    System.out.println("Eventos registrados de la wallet propia:");
                    origenEvents.forEach(event -> System.out.println(event.toString()));
                    var walletPropia = Wallet.from(walletPropiaId, origenEvents);
                    System.out.println("Eventos registrados de la wallet destino:");
                    destinoEvents.forEach(event -> System.out.println(event.toString()));
                    var walletDestino = Wallet.from(walletDestinoid, destinoEvents);
                    var cantidad = realizarTransferencia.getValor();
                    var motivo = realizarTransferencia.getMotivo();

                    walletPropia.crearTransferencia(walletDestinoid, new TransferenciaID(), cantidad,
                        motivo);

                    walletDestino.crearTransferencia(walletDestinoid, new TransferenciaID(), cantidad,
                        motivo);

                    var cambiosPropios = Flux.fromIterable(walletPropia.getUncommittedChanges());
                    var cambiosDeDestino = Flux.fromIterable(walletDestino.getUncommittedChanges());

                    return Flux.concat(cambiosPropios, cambiosDeDestino);
                  });
            }));
  }
}
*/