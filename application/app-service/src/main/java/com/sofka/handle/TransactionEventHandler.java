package com.sofka.handle;

import com.sofka.business.usecase.TransfereciaExitosaUseCase;
import com.sofka.business.usecase.ValidarTransferenciaUseCase;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
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

  private final TransactionConfirmationHandle handleConfirmation;

  private final TransactionCreationHandle handleCreation;


  public TransactionEventHandler(Blockchain blockchain,
      ValidarTransferenciaUseCase validarTransferenciaUseCase,
      TransfereciaExitosaUseCase transfereciaExitosaUseCase, TransactionConfirmationHandle handleConfirmation,
      TransactionCreationHandle handleCreation) {
    this.blockchain = blockchain;
    this.validarTransferenciaUseCase = validarTransferenciaUseCase;
    this.transfereciaExitosaUseCase = transfereciaExitosaUseCase;
    this.handleConfirmation = handleConfirmation;
    this.handleCreation = handleCreation;
  }

  @EventListener
  public Mono<Void> onTransactionCreated(TransferenciaCreada event) {
    log.info("Procesando transferencia creada");

    return handleCreation.apply(validarTransferenciaUseCase.apply(Mono.just(event)));
  }

  //TODO este listener no esta haciendo nada, si tratamos de que publique los eventos de transaccion exitosa quedará en bucle.
  @EventListener
  public Mono<Void> onTransactionSuccess(TransferenciaExitosa event) {
    log.info("Procesando transferencia exitosa");

    return handleConfirmation.apply(transfereciaExitosaUseCase.apply(Mono.just(event)));
  }
}
