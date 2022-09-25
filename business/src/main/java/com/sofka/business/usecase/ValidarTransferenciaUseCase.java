package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.BlockchainRepository;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ValidarTransferenciaUseCase extends UseCaseForEvent<TransferenciaCreada> {


  private final WalletDomainEventRepository repository;

  private final BlockchainRepository repositoryBlockchain;

  public ValidarTransferenciaUseCase(WalletDomainEventRepository repository,
      BlockchainRepository repositoryBlockchain) {
    this.repository = repository;
    this.repositoryBlockchain = repositoryBlockchain;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaCreada> transferenciaCreadaMono) {
    return transferenciaCreadaMono.flatMapMany(transferenciaCreada ->
        repository.obtenerEventos(transferenciaCreada.aggregateRootId()).collectList()
            .flatMapMany(
                eventsWalletOrigen -> getEventsWalletDestino(transferenciaCreada).collectList()
                    .flatMapMany(eventsWalletDestino -> {

                      var walletOrigenID = WalletID.of(transferenciaCreada.aggregateRootId());
                      var walletOrigen = Wallet.from(walletOrigenID, eventsWalletOrigen);

                      var walletDestinoID = WalletID.of(
                          transferenciaCreada.getWalletDestino().value());
                      var walletDestino = Wallet.from(walletDestinoID, eventsWalletDestino);

                      var cantidad = transferenciaCreada.getValor();

                      var cantidadWalletOrigen = new Cantidad(cantidad.value() * -1);

                      walletDestino.ModificarSaldo(walletDestinoID, cantidad);

                      walletOrigen.ModificarSaldo(walletOrigenID, cantidadWalletOrigen);

                      Flux<DomainEvent> uncommitedChangesWalletOrigen = Flux.fromIterable(
                          walletOrigen.getUncommittedChanges());

                      Flux<DomainEvent> uncommitedChangesWalletDestino = Flux.fromIterable(
                          walletDestino.getUncommittedChanges());

                      return Flux.concat(uncommitedChangesWalletDestino,
                          uncommitedChangesWalletOrigen);
                    })));
  }

  private Flux<DomainEvent> getEventsWalletDestino(TransferenciaCreada transferenciaCreada) {
    return repository.obtenerEventos(transferenciaCreada.getWalletDestino().value());
  }
}
