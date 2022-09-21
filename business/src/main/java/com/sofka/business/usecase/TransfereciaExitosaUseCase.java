package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransfereciaExitosaUseCase extends UseCaseForEvent<TransferenciaExitosa> {

  private final WalletDomainEventRepository repository;

  public TransfereciaExitosaUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaExitosa> transferenciaExitosaMono) {
    return transferenciaExitosaMono.flatMapMany(transferenciaExitosa ->
        repository.obtenerEventos(transferenciaExitosa.aggregateRootId())
            .collectList()
            .flatMapIterable(domainEvents -> {
              var wall = Wallet.from(WalletID.of(transferenciaExitosa.aggregateRootId()),
                  domainEvents);

              var walletId = WalletID.of(transferenciaExitosa.aggregateRootId());
              var cantidad = new Cantidad(transferenciaExitosa.getCantidad().value() * -1);

              wall.concretarTransferencia(transferenciaExitosa.getTransferenciaID());
              wall.ModificarSaldo(walletId, cantidad);

              return wall.getUncommittedChanges();
            })
    );
  }
}