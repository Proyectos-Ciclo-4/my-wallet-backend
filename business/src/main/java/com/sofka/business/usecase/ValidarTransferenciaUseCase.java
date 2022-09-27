package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ValidarTransferenciaUseCase extends UseCaseForEvent<TransferenciaCreada> {

  private final WalletDomainEventRepository repository;

  public ValidarTransferenciaUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaCreada> transferenciaCreadaMono) {
    return transferenciaCreadaMono.flatMapMany(
        transferenciaCreada -> getEventsWallet(transferenciaCreada).collectList()
            .flatMapMany(events -> {
              var walletId = WalletID.of(transferenciaCreada.aggregateRootId());
              var wallet = Wallet.from(walletId, events);
              var transfereciaId = transferenciaCreada.getTransferenciaID();
              var cantidad = transferenciaCreada.getWalletDestino().equals(walletId)
                  ? transferenciaCreada.getValor() : transferenciaCreada.getValor().negate();

              wallet.ModificarSaldo(walletId, cantidad, transfereciaId);

              return Flux.fromIterable(wallet.getUncommittedChanges());
            }));
  }

  private Flux<DomainEvent> getEventsWallet(TransferenciaCreada transferenciaCreada) {
    return repository.obtenerEventos(transferenciaCreada.aggregateRootId());
  }
}
