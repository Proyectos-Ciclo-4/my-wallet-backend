package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.AgregarMotivo;
import com.sofka.domain.wallet.eventos.MotivoCreado;
import com.sofka.domain.wallet.eventos.WalletCreada;
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
class AgregarMotivoUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private AgregarMotivoUseCase usecase;

  @Test
  void anadirMotivo() {
    var anadirMotivo = new AgregarMotivo("wallet1", "Viajes", "#FF0000");

    Mockito.when(repository.obtenerEventos("wallet1")).thenReturn(history());

    StepVerifier.create(usecase.apply(Mono.just(anadirMotivo))).expectNextMatches(domainEvent -> {
      var event = (MotivoCreado) domainEvent;
      return event.getMotivo().value().descripcion().equals("Viajes") && event.getMotivo().value()
          .color().equals("#FF0000");
    }).expectComplete().verify();

  }

  private Flux<DomainEvent> history() {

    var walletId = WalletID.of("wallet1");
    var usuarioId = UsuarioID.of("usuario1");
    var saldo = new Saldo(100.0);

    return Flux.just(new WalletCreada(walletId, usuarioId, saldo));
  }
}