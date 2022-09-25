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
                realizarTransferencia.getWalletDestino().value()).collectList()
            .flatMapIterable(domainEvents -> {
              var walletDestino = realizarTransferencia.getWalletDestino();
              var walletPropia = realizarTransferencia.getWalletOrigen();

              var wallet = Wallet.from(walletPropia, domainEvents);
              var cantidad = realizarTransferencia.getValor();
              var motivo = realizarTransferencia.getMotivo();

              wallet.crearTransferencia(walletDestino, TransferenciaID.of("z"), cantidad, motivo);

              return wallet.getUncommittedChanges();
            }));
  }
}