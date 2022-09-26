package com.sofka.generic.materialize;

import com.mongodb.client.result.UpdateResult;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
import com.sofka.generic.materialize.model.TransactionModel;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Mono;

@EnableAsync
@Configuration
@Slf4j
public class HistoryMaterializeHandler {

  private static final String COLLECTION_VIEW = "history";

  private final WalletMaterializeHandler walletDataHandler;

  private final ReactiveMongoTemplate template;

  public HistoryMaterializeHandler(ReactiveMongoTemplate template, WalletMaterializeHandler walletDataHandler) {
    this.template = template;
    this.walletDataHandler = walletDataHandler;
  }

  @EventListener
  public Mono<TransactionModel> handleTransferenciaRealiazada(
      TransferenciaCreada transferenciaCreada) {

    log.info("Materializando transferencia creada");
    var data = new TransactionModel(
        transferenciaCreada.getTransferenciaID().value(),
        transferenciaCreada.aggregateRootId(),
        transferenciaCreada.getWalletDestino().value(),
        transferenciaCreada.getValor().value(),
        transferenciaCreada.getMotivo().value(),
        LocalDateTime.now()
        );

    /*
    data.put("walletId", transferenciaCreada.aggregateRootId());
    data.put("transferencia_id", transferenciaCreada.getTransferenciaID().value());
    data.put("valor", transferenciaCreada.getValor().value());
    data.put("estado", transferenciaCreada.getEstadoDeTransferencia().value().name());
    data.put("destino", transferenciaCreada.getWalletDestino().value());
    data.put("motivo", transferenciaCreada.getMotivo().value());
    data.put("fecha", LocalDateTime.now());
     */
    //TODO esto se vuelve bloqueante en la 2da peticion.

    return template.save(data, COLLECTION_VIEW);
  }

  @EventListener
  public void handleTransferenciaFallida(TransferenciaFallida transfereciaFallida) {
    var update = new Update();
    update.set("estado", "RECHAZADA");

    template.updateFirst(
        filtrarPorIdDeTransferencia(transfereciaFallida.getTransferenciaID().value()), update,
        COLLECTION_VIEW).block();
  }

  @EventListener
  public Mono<UpdateResult> handleTransferenciaExitosa(TransferenciaExitosa transferenciaExitosa) {
    log.info("Materializando transferencia exitosa");

    var update = new Update();
    update.set("estado", "EXITOSA");

    return template.updateMulti(
        filtrarPorIdDeTransferencia(transferenciaExitosa.getTransferenciaID().value()), update,
        COLLECTION_VIEW);
  }


  private Query filtrarPorIdDeTransferencia(String transfereciaFallida) {
    return new Query(Criteria.where("transferencia_id").is(transfereciaFallida));
  }
}