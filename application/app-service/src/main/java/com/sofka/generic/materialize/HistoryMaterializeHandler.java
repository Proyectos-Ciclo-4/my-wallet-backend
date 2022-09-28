package com.sofka.generic.materialize;

import com.mongodb.client.result.UpdateResult;
import com.sofka.domain.wallet.eventos.MotivoCreado;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

  private final ReactiveMongoTemplate template;

  public HistoryMaterializeHandler(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public Mono<HashMap<Object, Object>> handleTransferenciaRealiazada(
      TransferenciaCreada transferenciaCreada) {

    log.info("Materializando transferencia creada {}", transferenciaCreada);
    var data = new HashMap<>();

    data.put("walletId", transferenciaCreada.aggregateRootId());
    data.put("transferencia_id", transferenciaCreada.getTransferenciaID().value());
    data.put("valor", transferenciaCreada.getValor().value());
    data.put("estado", transferenciaCreada.getEstadoDeTransferencia().value().name());
    data.put("destino", transferenciaCreada.getWalletDestino().value());
    data.put("motivo", transferenciaCreada.getMotivo().value());
    data.put("fecha", LocalDateTime.now());
    return template.save(data, COLLECTION_VIEW);
  }

  @EventListener
  public UpdateResult handleTransferenciaFallida(TransferenciaFallida transfereciaFallida) {
    var update = new Update();
    update.set("estado", "RECHAZADA");

    return template.updateFirst(
        filtrarPorIdDeTransferencia(transfereciaFallida.getTransferenciaID().value()), update,
        COLLECTION_VIEW).block();
  }

 /* @EventListener
  public void handleMotivoCreado(MotivoCreado motivoCreado) {
    return template.updateFirst(query, update, "");
  }*/

  @EventListener
  public Mono<UpdateResult> handleTransferenciaExitosa(TransferenciaExitosa transferenciaExitosa) {
    log.info("Materializando transferencia exitosa");

    Update updateWalletHistory = updateWalletHistory(transferenciaExitosa);

    var update = new Update();
    update.set("estado", transferenciaExitosa.getEstado().value().name());

    return template.updateMulti(
        filtrarPorIdDeTransferencia(transferenciaExitosa.getTransferenciaID().value()), update,
        COLLECTION_VIEW).flatMap(updateResult -> template.updateMulti(
        filtrarPorWalletsId(transferenciaExitosa.aggregateRootId()), updateWalletHistory,
        "wallet_data"));
  }

  @NotNull
  private static Update updateWalletHistory(TransferenciaExitosa transferenciaExitosa) {
    var updateWalletHistory = new Update();
    var data = new HashMap<>();
    data.put("walletId", transferenciaExitosa.aggregateRootId());
    data.put("transferencia_id", transferenciaExitosa.getTransferenciaID().value());
    data.put("valor", transferenciaExitosa.getValor().value());
    data.put("estado", transferenciaExitosa.getEstado().value().name());
    data.put("destino", transferenciaExitosa.getWalletDestino().value());
    data.put("motivo", transferenciaExitosa.getMotivo().value());
    data.put("fecha", LocalDateTime.now());
    updateWalletHistory.addToSet("historial", data);

    return updateWalletHistory;
  }

  private Query filtrarPorWalletsId(String id) {
    return new Query(Criteria.where("walletId").in(id));
  }

  private Query filtrarPorIdDeTransferencia(String transfereciaFallida) {
    return new Query(Criteria.where("transferencia_id").is(transfereciaFallida));
  }
}