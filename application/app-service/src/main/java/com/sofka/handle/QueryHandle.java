package com.sofka.handle;

import com.sofka.domain.wallet.Wallet;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.function.RouterFunctions;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;


public class QueryHandle {

  private final ReactiveMongoTemplate template;


  public QueryHandle(ReactiveMongoTemplate template) {
    this.template = template;
//    this.errorHandler = errorHandler;
  }

  /*
  @Bean
  public RouterFunction<ServerResponse> isCreated() {

    return RouterFunctions.route(
        GET("/wallet/{userId}"),
        request -> template.findOne(filterByUserId(request.pathVariable("userId")), Wallet.class, "wallet")

    )
  }

  @Bean
  public RouterFunction<ServerResponse> verifyUser(){
    return RouterFunctions.route(
        GET("/validate/user/").and(accept(MediaType.APPLICATION_JSON)),
        request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(, DomainEvent.class))
    );
  }
 */

  public Query filterByUserId(String userId) {
    return new Query(Criteria.where("userId").is(userId));
  }

//  public RouterFunction<ServerResponse> history() {
//    return RouterFunctions.route(GET("/history/{walletId}"),
//
//        request -> template.find(findByWalletId(request.pathVariable("walletId")),
//                HistoryListModel.class, "gameview").collectList().flatMap(
//                list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
//                    BodyInserters.fromPublisher(Flux.fromIterable(list), HistoryListModel.class)))
//            .onErrorResume(errorHandler::error));
//  }

  private void findByWalletId(String pathVariable) {
  }
}