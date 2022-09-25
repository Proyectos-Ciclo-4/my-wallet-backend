package com.sofka.handle;

import com.sofka.business.usecase.ValidarTransferenciaUseCase;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
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

  private final ValidarTransferenciaUseCase useCase;

  private final IntegrationHandle handle;

  public TransactionEventHandler(Blockchain blockchain, ValidarTransferenciaUseCase useCase,
      IntegrationHandle handle) {
    this.blockchain = blockchain;
    this.useCase = useCase;
    this.handle = handle;
  }

  @EventListener
  public void onTransactionCreated(TransferenciaCreada event) {
    log.info("Procesando transferencia creada");

    handle.apply(useCase.apply(Mono.just(event))).block();
  }
}
