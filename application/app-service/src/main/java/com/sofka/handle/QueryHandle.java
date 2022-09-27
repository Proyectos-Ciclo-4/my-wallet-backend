package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.sofka.generic.materialize.model.UserModel;
import com.sofka.generic.materialize.model.WalletModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Configuration
public class QueryHandle {

  private final ReactiveMongoTemplate template;


  public QueryHandle(ReactiveMongoTemplate template) {
    this.template = template;
//    this.errorHandler = errorHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> isCreated() {
    return RouterFunctions.route(
        GET("/wallet/{walletId}"),
        request -> template.findOne(filterByWalletId(request.pathVariable("walletId")),
                WalletModel.class, "wallet_data")
            .flatMap(element -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(element), WalletModel.class)))
    );
  }

  @Bean
  public RouterFunction<ServerResponse> findByPhoneNumber() {
    return RouterFunctions.route(
        GET("/wallet/{telefono}"),
        request -> template.findOne(filterByPhoneNumber(request.pathVariable("telefono")),
                UserModel.class, "usuarios")
            .flatMap(element -> {
                  System.out.println(element);

                  return ServerResponse.ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class));
                }
            ));
  }

  private Query filterByWalletId(String userId) {
    return new Query(Criteria.where("usuario").is(userId));
  }

  private Query filterByPhoneNumber(String phoneNumber) {
    return new Query(Criteria.where("numero").is(phoneNumber));
  }
}