package com.sofka.generic.handle;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

public class CommandHandler {

  @Bean
  public RouterFunctions<ServerResponse> create() {
    return null;
  }

}
