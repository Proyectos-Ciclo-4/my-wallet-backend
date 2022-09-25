package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.AnadirContacto;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AnadirContactoUseCase extends UseCaseForCommand<AnadirContacto>{

  private final WalletDomainEventRepository walletsRepository;

  public AnadirContactoUseCase(WalletDomainEventRepository walletsRepository) {
    this.walletsRepository = walletsRepository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<AnadirContacto> anadirContactoMono) {
    return anadirContactoMono.flatMapMany(comando -> walletsRepository.obtenerEventos(comando.getWalletID()).collectList().flatMapMany(
        events -> {
          var wallet = Wallet.from(WalletID.of(comando.getWalletID()),events);
          var nombreContacto = new Nombre(comando.getNombre());
          var emailContacto = new Email(comando.getEmail());
          var telefonoContacto = new Telefono(comando.getTelefono());

          wallet.anadirContacto(nombreContacto,emailContacto,telefonoContacto);

          return Flux.fromIterable(wallet.getUncommittedChanges());
        }));
  }
}
