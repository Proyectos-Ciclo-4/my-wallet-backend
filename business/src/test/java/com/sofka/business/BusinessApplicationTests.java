package com.sofka.business;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.CrearWalletUseCase;
import com.sofka.business.usecase.gateway.UsuarioRepositorio;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.ArrayList;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BusinessApplicationTests {

  @Mock
  private WalletDomainEventRepository repository;
  @Mock
  private UsuarioRepositorio usuarioRepositorio;
  @InjectMocks
  private CrearWalletUseCase useCase;

  @Test
  void crearWallet() {
    var walletId = WalletID.of("wallet1");
    var usuarioId = UsuarioID.of("usuario1");
    var saldo = new Saldo(100.0);
    var listaMotivos = new ArrayList<Motivo>();
    var motivo1 = new Motivo("recibo agua");
    var motivo2 = new Motivo("pago energia");
    listaMotivos.add(motivo1);
    listaMotivos.add(motivo2);
    var command = new CrearWallet(walletId, usuarioId, saldo, listaMotivos);

    //   when(repository.obtenerEventos("wallet1")).thenReturn(history());
    when(usuarioRepositorio.obtenerDatosUsuario("usuario1")).thenReturn(history2());

    StepVerifier.create(useCase.apply(Mono.just(command))).expectNextMatches(domainEvent -> {
      var evento = (WalletCreada) domainEvent;
      return "wallet1".equals(evento.getWalletID().value());
    }).expectNextMatches(domainEvent -> {
      var evento = (UsuarioAsignado) domainEvent;
      return "usuario1".equals(evento.getUsuarioID().value());
    }).expectComplete().verify();


  }

  private Mono<Usuario> history2() {
    var nombre = new Nombre("luisa");
    var telefono = new Telefono("3255555");
    var email = new Email("luisa@gmail.com");
    var ususarioId = UsuarioID.of("usuario1");
    return Mono.just(

        new Usuario(ususarioId, nombre, email, telefono));
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
