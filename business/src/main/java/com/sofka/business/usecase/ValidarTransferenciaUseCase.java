package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.BlockchainRepository;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.concurrent.TimeUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ValidarTransferenciaUseCase extends UseCaseForEvent<TransferenciaCreada>{

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaCreada> transferenciaCreadaMono) {
    return null;
  }
}


/*{

  private final WalletDomainEventRepository repository;

  private final BlockchainRepository repositoryBlockchain;

  public ValidarTransferenciaUseCase(WalletDomainEventRepository repository,
      BlockchainRepository repositoryBlockchain) {
    this.repository = repository;
    this.repositoryBlockchain = repositoryBlockchain;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaCreada> transferenciaCreadaMono) {

    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return transferenciaCreadaMono.flatMap(transferenciaCreada -> {
      repository
          .obtenerEventos(transferenciaCreada.aggregateRootId())
          .collectList()
          .flatMap(eventsWalletOrigen -> {

            var walletOrigenID = WalletID.of(transferenciaCreada.aggregateRootId());
            var walletOrigen = Wallet.from(walletOrigenID,eventsWalletOrigen);

            var transferenciaBlockchainID =

            var transferenciaBlockchain = repositoryBlockchain.getFromBlockchain()





          })
    })
  }
}*/
