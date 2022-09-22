package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RealizarTransferenciaUseCase extends UseCaseForCommand<RealizarTransferencia> {

  private final WalletDomainEventRepository repository;

  public RealizarTransferenciaUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<RealizarTransferencia> realizarTransferenciaMono) {
    return realizarTransferenciaMono.flatMapMany(realizarTransferencia ->
        repository.obtenerEventos(realizarTransferencia.getWalletDestino().value())
            .collectList()
            .flatMapIterable(domainEvents -> {
              var walletId = realizarTransferencia.getWalletDestino();
              var wallet = Wallet.from(walletId, domainEvents);
              var transferenciaID = realizarTransferencia.getTransferenciaID();
              var cantidad = realizarTransferencia.getValor();
              var motivo = realizarTransferencia.getMotivo();

              wallet.crearTransferencia(walletId, transferenciaID, cantidad, motivo);

              return wallet.getUncommittedChanges();
            })
    );
  }
}