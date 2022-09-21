package com.sofka.business.usecase;

import static org.junit.jupiter.api.Assertions.*;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.comandos.AnadirMotivo;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.eventos.MotivoCreado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.ArrayList;
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
class AnadirMotivoUseCaseTest {

  @Mock
  private WalletDomainEventRepository repository;

  @InjectMocks
  private AnadirMotivoUseCase usecase;

  @Test
  void anadirMotivo() {

    var anadirMotivo = new AnadirMotivo(WalletID.of("wallet1"), new Motivo("Viajes"));

    Mockito.when(repository.obtenerEventos("wallet1")).thenReturn(history());

    StepVerifier.create(usecase.apply(Mono.just(anadirMotivo))).expectNextMatches(domainEvent -> {
      var event = (MotivoCreado) domainEvent;
      return event.getDescripcion().value().equals("Viajes");
    }).expectComplete().verify();

  }

  private Flux<DomainEvent> history() {

    var walletId = WalletID.of("wallet1");
    var usuarioId = UsuarioID.of("usuario1");
    var saldo = new Saldo(100.0);
    var listaMotivos = new ArrayList<Motivo>();
    var motivo1 = new Motivo("recibo agua");
    var motivo2 = new Motivo("pago energia");
    listaMotivos.add(motivo1);
    listaMotivos.add(motivo2);

    return Flux.just(new WalletCreada(walletId, usuarioId, saldo, listaMotivos));
  }

}