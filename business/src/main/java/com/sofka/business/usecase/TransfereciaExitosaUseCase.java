package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransfereciaExitosaUseCase extends UseCaseForEvent<TransferenciaValidada> {

  private final WalletDomainEventRepository repository;

  public TransfereciaExitosaUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaValidada> transferenciaValidadaMono) {
    return transferenciaValidadaMono.flatMapMany(
        transferenciaValidada -> repository.obtenerEventos(transferenciaValidada.aggregateRootId())
            .collectList().flatMapIterable(domainEvents -> {
              var wall = Wallet.from(WalletID.of(transferenciaValidada.aggregateRootId()),
                  domainEvents);

              wall.concretarTransferencia(transferenciaValidada.getWalletOrigen(),
                  transferenciaValidada.getWalletDestino(),
                  transferenciaValidada.getTransferenciaID(), transferenciaValidada.getValor(),
                  transferenciaValidada.getMotivo(), new Estado(TipoDeEstado.EXITOSA));

              return wall.getUncommittedChanges();
            }));
  }
}