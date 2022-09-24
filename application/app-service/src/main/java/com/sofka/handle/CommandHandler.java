package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.business.usecase.CrearWalletUseCase;
import com.sofka.business.usecase.RealizarTransferenciaUseCase;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.domain.wallet.comandos.RealizarTransferencia;
import com.sofka.generic.helpers.Validators;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandHandler {

  private final RegisterIntegrationHandle integrationHandle;

  private final Validators validators;

  public CommandHandler(RegisterIntegrationHandle integrationHandle, Validators validators) {
    this.integrationHandle = integrationHandle;
    this.validators = validators;
  }

//  private final Error

  @Bean
  public RouterFunction<ServerResponse> create(CrearWalletUseCase useCase) {
    return route(
        POST("/new/wallet").and(accept(MediaType.APPLICATION_JSON)),
        request -> useCase.andThen(integrationHandle)
            .apply(validators.validateUser(request.bodyToMono(CrearWallet.class)))
            .then(ServerResponse.ok().build()));
//            .onErrorResume(errorHandler::error)
  }

  @Bean
  public RouterFunction<ServerResponse> transaction(RealizarTransferenciaUseCase useCase) {
    return route(
        POST("/new/transaction/").and(accept(MediaType.APPLICATION_JSON)),
        request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(useCase.apply(request.bodyToMono(RealizarTransferencia.class)), DomainEvent.class)));
//            .onErrorResume(errorHandler::error)
  }
}