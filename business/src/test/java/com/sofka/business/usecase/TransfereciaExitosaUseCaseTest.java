package com.sofka.business.usecase;

import static org.mockito.Mockito.when;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransfereciaExitosaUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private TransfereciaExitosaUseCase useCase;

  @Test
  void transferenciaExitosa() {
    var walletOrigen = WalletID.of("w2");
    var walletDestino = WalletID.of("w1");
    var transferenciaId = new TransferenciaID();
    var cantidad = new Cantidad(100.0);
    var motivo = new Motivo("Viajes", "#FF0000");
    var estado = new Estado(TipoDeEstado.EXITOSA);

    var transferenciaValidada = new TransferenciaValidada(walletOrigen, walletDestino,
        transferenciaId, cantidad, motivo, estado);
    transferenciaValidada.setAggregateRootId("w2");

    when(repository.obtenerEventos(transferenciaValidada.aggregateRootId())).thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(transferenciaValidada)))
        .expectNextMatches(domainEvent -> {
          var evento = (TransferenciaExitosa) domainEvent;

          return evento.getEstado().value().equals(TipoDeEstado.EXITOSA);
        }).expectComplete().verify();
  }

  private Flux<DomainEvent> history() {
    var walletId = WalletID.of("w1");
    var usuarioId = UsuarioID.of("w1");
    var saldo = new Saldo(100.0);

    var walletCreada = new WalletCreada(walletId, usuarioId, saldo);
    var walletOrigen = WalletID.of("w1");
    var walletDestino = WalletID.of("w1");
    var cantidad = new Cantidad(100.0);
    var motivo = new Motivo("Motivo 1", "#FF0000");
    walletCreada.setAggregateRootId(walletId.value());

    var transferenciaCreada = new TransferenciaCreada(walletOrigen, walletDestino, cantidad, motivo
    );
    transferenciaCreada.setAggregateRootId(walletId.value());

    return Flux.just(walletCreada, transferenciaCreada);
  }
}