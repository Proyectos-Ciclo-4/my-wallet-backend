package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.ArrayList;
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
        transferenciaCreada -> repository.obtenerEventos(transferenciaCreada.aggregateRootId())
            .collectList().flatMapMany(
                eventsWalletOrigen -> getEventsWalletDestino(transferenciaCreada).collectList()
                    .flatMapMany(eventsWalletDestino -> {

                      var walletOrigenID = WalletID.of(transferenciaCreada.aggregateRootId());
                      var walletOrigen = Wallet.from(walletOrigenID, eventsWalletOrigen);

                      var walletDestinoID = WalletID.of(
                          transferenciaCreada.getWalletDestino().value());

                      var walletDestino = Wallet.from(walletDestinoID, eventsWalletDestino);

                      var cantidad = transferenciaCreada.getValor();

                      var cantidadWalletOrigen = new Cantidad(cantidad.value() * -1);

                      walletDestino.ModificarSaldo(walletDestinoID, cantidad,
                          transferenciaCreada.getTransferenciaID());

                      walletOrigen.ModificarSaldo(walletOrigenID, cantidadWalletOrigen,
                          transferenciaCreada.getTransferenciaID());

                      var cambiosOrigen = walletOrigen.getUncommittedChanges();
                      var cambiosDestino = walletDestino.getUncommittedChanges();
                      var cambios = new ArrayList<>(cambiosOrigen);

                      var transferenciaValida1 = new TransferenciaValidada(
                          transferenciaCreada.getTransferenciaID());

                      var transferenciaValida2 = new TransferenciaValidada(
                          transferenciaCreada.getTransferenciaID());

                      transferenciaValida1.setAggregateRootId(
                          transferenciaCreada.aggregateRootId());

                      transferenciaValida2.setAggregateRootId(
                          transferenciaCreada.getWalletDestino().value());

                      cambios.addAll(new ArrayList<>(cambiosDestino));
                      cambios.add(transferenciaValida1);
                      cambios.add(transferenciaValida2);

                      return Flux.fromIterable(cambios);
                    })));
  }

  private Flux<DomainEvent> getEventsWalletDestino(TransferenciaCreada transferenciaCreada) {
    return repository.obtenerEventos(transferenciaCreada.getWalletDestino().value());
  }
}
