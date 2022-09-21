package com.sofka.generic.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.sofka.generic.handle.model.HistoryListModel;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

public class QueryHandle {

  private final ReactiveMongoTemplate template;


  public QueryHandle(ReactiveMongoTemplate template) {
    this.template = template;
    this.errorHandler = errorHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> history() {
    return RouterFunctions.route(GET("/history/{walletId}"),

        request -> template.find(findByWalletId(request.pathVariable("walletId")),
                HistoryListModel.class, "gameview").collectList().flatMap(
                list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                    BodyInserters.fromPublisher(Flux.fromIterable(list), HistoryListModel.class)))
            .onErrorResume(errorHandler::error));
  }

  private void findByWalletId(String pathVariable) {
  }
}