package com.sofka.generic.materialize;

import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Configuration
public class HistoryMaterializeHandler {

  private static final String COLLECTION_VIEW = "history";

  private final ReactiveMongoTemplate template;

  public HistoryMaterializeHandler(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public void handleTransferenciaRealiazada(TransferenciaCreada transferenciaCreada) {
    var data = new HashMap<>();

    data.put("_id", transferenciaCreada.aggregateRootId());
    data.put("transferencia_id", transferenciaCreada.getTransferenciaID().value());
    data.put("valor", transferenciaCreada.getValor().value());
    data.put("estado", transferenciaCreada.getEstadoDeTransferencia().value().name());
    data.put("destino", transferenciaCreada.getWalletDestino().value());
    data.put("motivo", transferenciaCreada.getMotivo().value());
    data.put("fecha", LocalDateTime.now());
    template.save(data, COLLECTION_VIEW).block();
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
  public void handleTransferenciaExitosa(TransferenciaExitosa transferenciaExitosa) {
    var update = new Update();
    update.set("estado", "EXITOSA");

    template.updateFirst(
        filtrarPorIdDeTransferencia(transferenciaExitosa.getTransferenciaID().value()), update,
        COLLECTION_VIEW).block();
  }

  private Query filtrarPorIdDeTransferencia(String transfereciaFallida) {
    return new Query(Criteria.where("transferencia_id").is(transfereciaFallida));
  }
}