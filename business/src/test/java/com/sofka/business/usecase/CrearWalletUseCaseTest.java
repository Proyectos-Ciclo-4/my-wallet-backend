package com.sofka.business.usecase;

import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrearWalletUseCaseTest {

  @InjectMocks
  private CrearWalletUseCase useCase;

  @Test
  void crearWallet() {
    var telefono = new Telefono("123456789");
    var usuarioId = UsuarioID.of("usuario1");
    var email = new Email("david@gmail.com");

    var command = new CrearWallet(usuarioId, telefono, email);

    StepVerifier.create(useCase.apply(Mono.just(command))).expectNextMatches(domainEvent -> {
      var evento = (WalletCreada) domainEvent;

      return "usuario1".equals(evento.getWalletID().value());
    }).expectNextMatches(domainEvent -> {
      var evento = (UsuarioAsignado) domainEvent;

      return "usuario1".equals(evento.getUsuarioID().value());
    }).expectComplete().verify();
  }
}