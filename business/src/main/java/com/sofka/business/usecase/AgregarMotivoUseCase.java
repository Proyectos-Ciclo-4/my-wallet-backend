package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.AgregarMotivo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AgregarMotivoUseCase extends UseCaseForCommand<AgregarMotivo> {

  private final WalletDomainEventRepository repository;

  public AgregarMotivoUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<AgregarMotivo> agregarMotivoMono) {
    return agregarMotivoMono.flatMapMany(command -> repository.obtenerEventos(command.getWalletID())
        .collectList().flatMapMany(events -> {
          var wallet = Wallet.from(WalletID.of(command.getWalletID()), events);
          wallet.agregarMotivo(command.getDescripcion(), command.getColor());

          return Flux.fromIterable(wallet.getUncommittedChanges());
        }));
  }
}