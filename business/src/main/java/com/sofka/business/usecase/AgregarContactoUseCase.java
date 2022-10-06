package com.sofka.business.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.gateway.WalletDomainEventRepository;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.Wallet;
import com.sofka.domain.wallet.comandos.AgregarContacto;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AgregarContactoUseCase extends UseCaseForCommand<AgregarContacto> {

  private final WalletDomainEventRepository repository;

  public AgregarContactoUseCase(WalletDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<AgregarContacto> agregarContactoMono) {
    return agregarContactoMono.flatMapMany(
        command -> repository.obtenerEventos(command.getWalletId()).collectList()
            .flatMapIterable(domainEvents -> {
              var wallet = Wallet.from(WalletID.of(command.getWalletId()), domainEvents);
              wallet.nuevoContacto(WalletID.of(command.getWalletId()),
                  new Usuario(UsuarioID.of(command.getWalletId()), command.getNombre(),
                      command.getEmail(),
                      command.getTelefono()));

              return wallet.getUncommittedChanges();
            }));
  }
}
