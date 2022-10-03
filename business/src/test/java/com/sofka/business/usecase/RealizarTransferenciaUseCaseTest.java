package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
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
class RealizarTransferenciaUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private RealizarTransferenciaUseCase useCase;

  @Test
  void realiazarTransferenciaTest() {
    var walletOrigen = WalletID.of("w2");
    var walletDestino = WalletID.of("w1");
    var motivo = new Motivo("Pago de servicios", "#FF0000");
    var cantidad = new Cantidad(100.0);

    var realizarTransferencia = new RealizarTransferencia(walletOrigen, walletDestino, cantidad,
        motivo);

    Mockito.when(repository.obtenerEventos("w1")).thenReturn(history());
    Mockito.when(repository.obtenerEventos("w2")).thenReturn(history2());

    StepVerifier.create(useCase.apply(Mono.just(realizarTransferencia)))
        .expectNextMatches(domainEvent -> {
          var evento = (TransferenciaCreada) domainEvent;

          return evento.getWalletDestino().value().equals("w1") && evento.getEstadoDeTransferencia()
              .value().equals(TipoDeEstado.PENDIENTE) && evento.getValor().value().equals(-100.0);
        }).expectNextMatches(domainEvent -> {
          var evento = (TransferenciaCreada) domainEvent;

          return evento.getWalletOrigen().value().equals("w2") && evento.getEstadoDeTransferencia()
              .value().equals(TipoDeEstado.PENDIENTE) && evento.getValor().value().equals(100.0);
        }).verifyComplete();
  }

  private Flux<DomainEvent> history2() {
    var walletId = WalletID.of("w2");
    var usuarioId = UsuarioID.of("w2");
    var saldo = new Saldo(100.0);

    var walletCreada = new WalletCreada(walletId, usuarioId, saldo);

    return Flux.just(walletCreada);
  }

  private Flux<DomainEvent> history() {
    var walletId = WalletID.of("w1");
    var usuarioId = UsuarioID.of("w1");
    var saldo = new Saldo(100.0);

    var walletCreada = new WalletCreada(walletId, usuarioId, saldo);

    return Flux.just(walletCreada);
  }
}