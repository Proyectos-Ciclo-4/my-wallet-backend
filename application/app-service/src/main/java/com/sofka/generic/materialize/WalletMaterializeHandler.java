package com.sofka.generic.materialize;

import com.mongodb.client.result.UpdateResult;
import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class WalletMaterializeHandler {

  private static final String COLLECTION_VIEW = "wallet_data";

  private final ReactiveMongoTemplate template;

  public WalletMaterializeHandler(ReactiveMongoTemplate template) {
    this.template = template;
  }

  //TODO modificar materializacion para añadir las ultimas 3 transacciones y solo las ultimas 3.

  @EventListener
  public Mono<HashMap<Object, Object>> handleWalletCreada(WalletCreada walletCreada) {
    log.info("Materializing WalletCreada event: {}", walletCreada);
    var data = new HashMap<>();

    data.put("walletId", walletCreada.getWalletID().value());
    data.put("usuario", walletCreada.getUsuarioID().value());
    data.put("motivos", new ArrayList<>(List.of("Indefinido")));
    data.put("contactos", new ArrayList<>());
    data.put("ultimasTransacciones", new ArrayList<>());
    data.put("saldo", walletCreada.getSaldo().value());

    return template.save(data, COLLECTION_VIEW);
  }

  @EventListener
  public Mono<Object> handleUsuarioAsignado(UsuarioAsignado usuarioAsignado) {
    log.info("Materializing UsuarioAsignado event: {}", usuarioAsignado);

    var update = new Update();
    HashMap<String, String> usuario = new HashMap<>();

    update.set("usuario", usuarioAsignado.getUsuarioID().value());

    usuario.put("usuarioId", usuarioAsignado.getUsuarioID().value());
    usuario.put("email", usuarioAsignado.getEmail().value());
    usuario.put("numero", usuarioAsignado.getNumero().value());

    return template.updateFirst(filtrarPorIdDeWallet(usuarioAsignado.aggregateRootId()), update,
        COLLECTION_VIEW).flatMap(updateResult ->
        template.save(usuario, "usuarios"));
  }

  @EventListener
  public Mono<UpdateResult> handleSaldoModificado(SaldoModificado saldoModificado) {
    log.info("Materializing SaldoModificado event: {}", saldoModificado);
    var update = new Update();
    update.inc("saldo", saldoModificado.getCantidad().value());

    return template.updateFirst(filtrarPorIdDeWallet(saldoModificado.aggregateRootId()), update,
        COLLECTION_VIEW);
  }

  private Query filtrarPorIdDeWallet(String walletId) {
    return new Query(Criteria.where("walletId").is(walletId));
  }
}