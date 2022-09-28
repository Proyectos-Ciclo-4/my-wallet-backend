package com.sofka.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.sofka.adapters.repositories.EventStoreRepository;
import com.sofka.generic.materialize.model.TransaccionDeHistorial;
import com.sofka.generic.materialize.model.UserModel;
import com.sofka.generic.materialize.model.WalletModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    this.verifierRepo = verifierRepo;
  }

  @Bean
  public RouterFunction<ServerResponse> getNumberByUid() {
    return RouterFunctions.route(GET("/telefono/{uid}"),
        request -> template.findOne(filterByUid(request.pathVariable("uid")),
            UserModel.class, "usuarios").flatMap(
            element -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class))));
  }

  @Bean
  public RouterFunction<ServerResponse> isCreated() {
    return RouterFunctions.route(GET("/wallet/{walletId}"),
        request -> template.findOne(filterByWalletId(request.pathVariable("walletId")),
            WalletModel.class, "wallet_data").flatMap(
            element -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(element), WalletModel.class))));
  }

  @Bean
  public RouterFunction<ServerResponse> userExistsNumber() {
    return RouterFunctions.route(GET("/walletByTelefono/{telefono}"),
        request -> template.findOne(filterByPhoneNumber(request.pathVariable("telefono")),
            UserModel.class, "usuarios").flatMap(element -> {
          System.out.println(element);
          return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class));
        }));
  }

  @Bean
  public RouterFunction<ServerResponse> userExistsEmail() {
    return RouterFunctions.route(GET("/walletByEmail/{email}"),
        request -> template.findOne(filterByEmail(request.pathVariable("email")), UserModel.class,
                "usuarios")
            .flatMap(element -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(element), UserModel.class))));
  }

  @Bean
  public RouterFunction<ServerResponse> userBothValidation() {
    return RouterFunctions.route(GET("/validateBoth/{telefono}/email/{email}"),
        request -> verifierRepo.userExists(request.pathVariable("email"),
                request.pathVariable("telefono"))
            .flatMap(aBoolean -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(aBoolean), Boolean.class))));
  }

  @Bean
  RouterFunction<ServerResponse> historial() {
    return RouterFunctions.route(GET("/history/{from}/to/{to}/of/{Id}"),
        request -> template.find(
                filterByDate(request.pathVariable("from"), request.pathVariable("to"),
                    request.pathVariable("Id")),
                TransaccionDeHistorial.class, "history")
            .collectList()
            .flatMap(
                historial -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromPublisher(Mono.just(historial), List.class))));
  }

  private Query filterByDate(String date1, String date2, String Id) {
    var format = new SimpleDateFormat("yyyy-MM-dd");
    Date dateOne = null;
    Date dateTwo = null;

    try {
      dateOne = format.parse(date1);
      dateTwo = format.parse(date2);

    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return new Query(Criteria.where("fecha").gte(dateOne).lte(dateTwo).and("walletId").is(Id));
  }

  private Query filterByWalletId(String userId) {
    return new Query(Criteria.where("usuario").is(userId));
  }

  private Query filterByUid(String userId) {
    return new Query(Criteria.where("usuarioId").is(userId));
  }

  private Query filterByPhoneNumber(String phoneNumber) {
    return new Query(Criteria.where("numero").is(phoneNumber));
  }

  private Query filterByEmail(String email) {
    return new Query(Criteria.where("email").is(email));
  }
}