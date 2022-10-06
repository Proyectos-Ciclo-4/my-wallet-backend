package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.EliminarContacto;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BorrarContactoUseCase extends UseCaseForCommand<EliminarContacto> {

  private final WalletDomainEventRepository repository;

  public BorrarContactoUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<EliminarContacto> eliminarContactoMono) {
    return eliminarContactoMono.flatMapMany(eliminarContacto ->
        repository.obtenerEventos(eliminarContacto.getWalletId())
            .collectList()
            .flatMapIterable(events -> {
              var wallet = Wallet.from(WalletID.of(eliminarContacto.getWalletId()), events);

              wallet.eliminarContacto(eliminarContacto.getWalletId(),
                  eliminarContacto.getContactoId());

              return wallet.getUncommittedChanges();
            }));
  }
}
