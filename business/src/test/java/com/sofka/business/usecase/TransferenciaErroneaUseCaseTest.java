package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
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
class TransferenciaErroneaUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private TransferenciaErroneaUseCase useCase;

  @Test
  void transferenciaErronea() {
    var transferenciaFallida = new TransferenciaFallida(walletOrigen, walletDestino,
        TransferenciaID.of("xxx-xxx"), tipoDeMovimiento, estadoDeTransferencia, valor, motivo);
    transferenciaFallida.setAggregateRootId("w1");

    Mockito.when(repository.obtenerEventos(transferenciaFallida.aggregateRootId()))
        .thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(transferenciaFallida)))
        .expectNextMatches(domainEvent -> {
          var evento = (TransferenciaFallida) domainEvent;

          return "xxx-xxx".equals(evento.getTransferenciaID().value());
        }).verifyComplete();
  }

  private Flux<DomainEvent> history() {
    var walletId = WalletID.of("w1");
    var usuarioId = UsuarioID.of("u1");
    var saldo = new Saldo(100.0);
    var listaMotivos = List.of(new Motivo("Motivo 1"));

    var walletCreada = new WalletCreada(walletId, usuarioId, saldo, listaMotivos);

    var walletDestino = WalletID.of("w1");
    var transferenciaId = TransferenciaID.of("xxx-xxx");
    var estado = new Estado(TipoDeEstado.PENDIENTE);
    var cantidad = new Cantidad(100.0);
    var motivo = new Motivo("Motivo 1");

    var transferenciaCreada = new TransferenciaCreada(walletDestino, transferenciaId, estado,
        cantidad, motivo);

    return Flux.just(walletCreada, transferenciaCreada);
  }
}