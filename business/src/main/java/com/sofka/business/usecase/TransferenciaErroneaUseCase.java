package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransferenciaErroneaUseCase extends UseCaseForEvent<TransferenciaFallida> {

  private final WalletDomainEventRepository repository;

  public TransferenciaErroneaUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<TransferenciaFallida> transferenciaFallidaMono) {
    return transferenciaFallidaMono.flatMapMany(
        transferenciaFallida -> repository.obtenerEventos(transferenciaFallida.aggregateRootId())
            .collectList().flatMapIterable(domainEvents -> {
              var walletId = WalletID.of(transferenciaFallida.aggregateRootId());
              var wallet = Wallet.from(walletId, domainEvents);
              var transferenciaId = transferenciaFallida.getTransferenciaID();

              var walletOrigen = transferenciaFallida.getWalletOrigen();
              var walletDestino = transferenciaFallida.getWalletDestino();
              var transferenciaID = transferenciaFallida.getTransferenciaID();
              var estadoDeTransferencia = new Estado(TipoDeEstado.RECHAZADA);
              var valor = transferenciaFallida.getValor();
              var motivo = transferenciaFallida.getMotivo();

              wallet.cancelarTransferencia(walletOrigen, walletDestino, transferenciaID,
                  estadoDeTransferencia, valor, motivo);

              return wallet.getUncommittedChanges();
            }));
  }
}
