package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.comandos.BorrarWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BorrarWalletUseCase extends UseCaseForCommand<BorrarWallet> {

  @Override
  public Flux<DomainEvent> apply(Mono<BorrarWallet> borrarWalletMono) {
    return null;
  }
}
