package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.sofka.business.usecase.AgregarMotivoUseCase;
import com.sofka.business.usecase.CrearWalletUseCase;
import com.sofka.business.usecase.RealizarTransferenciaUseCase;
import com.sofka.domain.wallet.comandos.AgregarMotivo;
import com.sofka.domain.wallet.comandos.CrearWallet;
import com.sofka.generic.helpers.Validators;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandHandler {

  private final RegisterIntegrationHandle integrationHandle;

  private final IntegrationHandle generalHandle;

  private final Validators validators;

  public CommandHandler(RegisterIntegrationHandle integrationHandle,
      IntegrationHandle generalHandle, Validators validators) {
    this.integrationHandle = integrationHandle;
    this.generalHandle = generalHandle;
    this.validators = validators;
  }

  @Bean
  public RouterFunction<ServerResponse> create(CrearWalletUseCase useCase) {
    return route(
        POST("/new/wallet").and(accept(MediaType.APPLICATION_JSON)),
        request -> useCase.andThen(integrationHandle)
            .apply(validators.validateUser(request.bodyToMono(CrearWallet.class)))
            .then(ServerResponse.ok().build()))
        .filter(handleRuntimeException());
  }

  @Bean
  public RouterFunction<ServerResponse> transaction(RealizarTransferenciaUseCase useCase) {
    return route(
        POST("/new/transaction/").and(accept(MediaType.APPLICATION_JSON)),
        request -> useCase.andThen(generalHandle)
            .apply(validators.validateWallet(request))
            .then(ServerResponse.ok().build()))
        .filter(handleRuntimeException());
  }

  @Bean
  public RouterFunction<ServerResponse> nuevoMotivo(AgregarMotivoUseCase useCase) {
    return route(
        POST("/new/motivo/").and(accept(MediaType.APPLICATION_JSON)),
        request -> useCase.andThen(generalHandle)
            .apply(request.bodyToMono(AgregarMotivo.class))
            .then(ServerResponse.ok().build()))
        .filter(handleRuntimeException());
  }

  private HandlerFilterFunction<ServerResponse, ServerResponse> handleRuntimeException() {
    return (request, next) -> next.handle(request)
        .onErrorResume(RuntimeException.class,
            e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
  }
}