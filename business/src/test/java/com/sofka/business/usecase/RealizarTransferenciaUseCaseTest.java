package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.List;
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
    var walletDestino = WalletID.of("w1");
    var transferenciaID = TransferenciaID.of("t1");
    var estadoDeTransferencia = new Estado(TipoDeEstado.PENDIENTE);
    var valor = new Cantidad(30.0);
    var motivo = new Motivo("Pago de servicios", color);

    var realizarTransferencia = new RealizarTransferencia(walletDestino, transferenciaID,
        estadoDeTransferencia, valor, motivo);

    Mockito.when(repository.obtenerEventos("w1")).thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(realizarTransferencia)))
        .expectNextMatches(domainEvent -> {
          var evento = (TransferenciaCreada) domainEvent;

          return evento.getTransferenciaID().value().equals("t1") &&
              evento.getWalletDestino().value().equals("w1") &&
              evento.getEstadoDeTransferencia().value().equals(TipoDeEstado.PENDIENTE) &&
              evento.getValor().value().equals(30.0);
        })
        .verifyComplete();

  }

  private Flux<DomainEvent> history() {
    var walletId = WalletID.of("w1");
    var usuarioId = UsuarioID.of("u1");
    var saldo = new Saldo(100.0);
    var listaMotivos = List.of(new Motivo("Motivo 1", color));

    var walletCreada = new WalletCreada(walletId, usuarioId, saldo, listaMotivos);

    return Flux.just(walletCreada);
  }
}