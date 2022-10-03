package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.BorrarWallet;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.eventos.WalletDesactivada;
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
class BorrarWalletUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private BorrarWalletUseCase usecase;

  @Test
  void anadirMotivo() {
    var borrarWallet = new BorrarWallet("wallet1");

    Mockito.when(repository.obtenerEventos("wallet1")).thenReturn(history());

    StepVerifier.create(usecase.apply(Mono.just(borrarWallet))).expectNextMatches(domainEvent -> {
      var event = (WalletDesactivada) domainEvent;

      return event.aggregateRootId().equals("wallet1");
    }).expectComplete().verify();
  }

  private Flux<DomainEvent> history() {

    var walletId = WalletID.of("wallet1");
    var usuarioId = UsuarioID.of("usuario1");
    var saldo = new Saldo(100.0);

    return Flux.just(new WalletCreada(walletId, usuarioId, saldo));
  }
}