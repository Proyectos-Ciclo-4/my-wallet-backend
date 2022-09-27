package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.sofka.adapters.repositories.EventStoreRepository;
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

  private final EventStoreRepository verifierRepo;

  public QueryHandle(ReactiveMongoTemplate template, EventStoreRepository verifierRepo) {
    this.template = template;
//    this.errorHandler = errorHandler;
    this.verifierRepo = verifierRepo;
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

  /*
  @Bean
  public RouterFunction<ServerResponse> findByPhoneNumber() {
    return RouterFunctions.route(
        GET("/walletExists/{telefono}"),
        request -> template.findOne(filterByPhoneNumber(request.pathVariable("telefono")),
                UserModel.class, "usuarios")
            .flatMap(element -> {
                  System.out.println(element);

                  return ServerResponse.ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class));
                }
            ));
  }*/

  @Bean
  public RouterFunction<ServerResponse> userExistsNumber() {
    return RouterFunctions.route(
        GET("/walletByTelefono/{telefono}"),
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

  @Bean
  public RouterFunction<ServerResponse> userExistsEmail() {
    return RouterFunctions.route(
        GET("/walletByEmail/{email}"),
        request -> template.findOne(filterByEmail(request.pathVariable("email")),
                UserModel.class, "usuarios")
            .flatMap(element -> {
                  return ServerResponse.ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class));
                }
            ));
  }

  //Retorna true si existe el correo O el telefono, CUALQUIERA DE LOS DOS
  @Bean
  public RouterFunction<ServerResponse> userBothValidation() {
    return RouterFunctions.route(
        GET("/validateBoth/{telefono}/email/{email}"),
        request -> verifierRepo.userExists(request.pathVariable("email"),request.pathVariable("telefono")).flatMap(
            aBoolean -> {
              return ServerResponse.ok()
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromPublisher(Mono.just(aBoolean),Boolean.class));
            })
    );
  }

  /*
  @Bean
  public RouterFunction<ServerResponse> uservalidation() {
    return RouterFunctions.route(
        GET("/validateTelefono/{telefono}"),
        request ->
            aBoolean -> {
              System.out.println(aBoolean);
              return ServerResponse.ok()
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromPublisher(Mono.just(aBoolean),Boolean.class));
            })
    );
  }*/

  /*
  @Bean
  public RouterFunction<ServerResponse> verifyUser(){
    return RouterFunctions.route(
        GET("/validate/user/").and(accept(MediaType.APPLICATION_JSON)),
        request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(, DomainEvent.class))
    );
  }
 */

  private Query filterByWalletId(String userId) {
    return new Query(Criteria.where("usuario").is(userId));
  }

  private Query filterByPhoneNumber(String phoneNumber) {
    return new Query(Criteria.where("numero").is(phoneNumber));
  }

  private Query filterByEmail(String email) {
    return new Query(Criteria.where("email").is(email));
  }
}