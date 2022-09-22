package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.sofka.business.usecase.CrearWalletUseCase;
import com.sofka.domain.wallet.comandos.CrearWallet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandHandler {

  private final IntegrationHandle integrationHandle;

  public CommandHandler(IntegrationHandle integrationHandle) {
    this.integrationHandle = integrationHandle;
  }

//  private final Error

  @Bean
  public RouterFunction<ServerResponse> create(CrearWalletUseCase useCase) {
    return route(
        POST("/new/wallet").and(accept(MediaType.APPLICATION_JSON)),

        request -> useCase.andThen(integrationHandle)
            .apply(request.bodyToMono(CrearWallet.class))
            .then(ServerResponse.ok().build())
//            .onErrorResume(errorHandler::error)
    );
  }
}