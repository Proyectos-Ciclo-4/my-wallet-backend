package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.BorrarWallet;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BorrarWalletUseCase extends UseCaseForCommand<BorrarWallet> {

  private final WalletDomainEventRepository repository;

  public BorrarWalletUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<BorrarWallet> borrarWalletMono) {
    return borrarWalletMono.flatMapMany(borrarWallet ->
        repository.obtenerEventos(borrarWallet.getWalletId())
            .collectList()
            .flatMapMany(events -> {
              if (events.isEmpty()) {
                throw new RuntimeException("No se puede borrar una wallet que no existe");
              }

              var wallet = Wallet.from(WalletID.of(borrarWallet.getWalletId()), events);
              wallet.desactivarWallet();

              return Flux.fromIterable(events);
            })
    );
  }
}