package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ValidarTransferenciaUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private ValidarTransferenciaUseCase usecase;

  @Test
  void validarTransferenciaTest() {
    var walletOrigen = new WalletID("wallet1");
    var walletDestino = new WalletID("wallet2");
    var valor = new Cantidad(100.0);
    var motivo = new Motivo("Viajes", "#FF0000");

    var transferenciaCreada = new TransferenciaCreada(walletOrigen, walletDestino, valor, motivo);
    transferenciaCreada.setAggregateRootId("wallet1");

    Mockito.when(repository.obtenerEventos(transferenciaCreada.aggregateRootId()))
        .thenReturn(history());

    StepVerifier.create(usecase.apply(Mono.just(transferenciaCreada)))
        .expectNextMatches(domainEvent -> {
          var event = (SaldoModificado) domainEvent;

          return event.getCantidad().value() == 100.0;
        })
        .expectNextMatches(domainEvent -> {
          var event = (TransferenciaValidada) domainEvent;

          return event.getEstado().value().equals(TipoDeEstado.PENDIENTE)
              && event.getWalletDestino().value().equals("wallet2");
        })
        .expectComplete().verify();
  }

  private Flux<DomainEvent> history() {

    var walletId = WalletID.of("wallet1");
    var usuarioId = UsuarioID.of("usuario1");
    var saldo = new Saldo(100.0);
    var walletCreada = new WalletCreada(walletId, usuarioId, saldo);
    walletCreada.setAggregateRootId("wallet1");

    return Flux.just(walletCreada);
  }
}