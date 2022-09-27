package com.sofka.handle;

import com.sofka.business.usecase.TransfereciaExitosaUseCase;
import com.sofka.business.usecase.ValidarTransferenciaUseCase;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.generic.Blockchain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Mono;

@EnableAsync
@Slf4j
@Configuration
public class TransactionEventHandler {

  private final Blockchain blockchain;

  private final ValidarTransferenciaUseCase validarTransferenciaUseCase;

  private final TransfereciaExitosaUseCase transfereciaExitosaUseCase;

  private final IntegrationHandle handle;

  public TransactionEventHandler(Blockchain blockchain,
      ValidarTransferenciaUseCase validarTransferenciaUseCase,
      TransfereciaExitosaUseCase transfereciaExitosaUseCase, IntegrationHandle handle) {

    this.blockchain = blockchain;
    this.validarTransferenciaUseCase = validarTransferenciaUseCase;
    this.transfereciaExitosaUseCase = transfereciaExitosaUseCase;
    this.handle = handle;
  }

  @EventListener
  public Mono<Void> onTransactionCreated(TransferenciaCreada event) {
    log.info("Procesando transferencia creada");

    return handle.apply(validarTransferenciaUseCase.apply(Mono.just(event)));
  }

  @EventListener
  public Mono<Void> onTransactionSuccess(TransferenciaValidada event) {
    log.info("Procesando transferencia exitosa");

    return handle.handleShortcuts(transfereciaExitosaUseCase.apply(Mono.just(event)));
  }
}
