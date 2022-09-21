package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.AnadirMotivo;
import com.sofka.domain.wallet.comandos.CrearWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AnadirMotivoUseCase extends UseCaseForCommand<AnadirMotivo> {

  private final WalletDomainEventRepository repository;

  public AnadirMotivoUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<AnadirMotivo> anadirMotivo) {
    return anadirMotivo.flatMapMany(command -> {
      return repository.obtenerEventos(command.getWalletID().value())
          .collectList().flatMapIterable(events -> {
            var wallet = Wallet.from(command.getWalletID(), events);
            wallet.anadirMotivo(command.getDescripcion());
            return wallet.getUncommittedChanges();
          });
    }).flatMap(domainEvent -> Flux.just(domainEvent));
  }
}
