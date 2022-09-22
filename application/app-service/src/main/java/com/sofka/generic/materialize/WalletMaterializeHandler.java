package com.sofka.generic.materialize;

import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Configuration
public class WalletMaterializeHandler {

  private static final String COLLECTION_VIEW = "wallet";

  private final ReactiveMongoTemplate template;

  public WalletMaterializeHandler(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public void handleWalletCreada(WalletCreada walletCreada) {
    var data = new HashMap<>();

    data.put("_id", walletCreada.getWalletID());
    data.put("usuario", walletCreada.getUsuarioID().value());
    data.put("motivos", new ArrayList<>(List.of("Indefinido")));
    data.put("saldo", walletCreada.getSaldo().value());

    template.save(data, COLLECTION_VIEW).block();
  }

  @EventListener
  public void handleUsuarioAsignado(UsuarioAsignado usuarioAsignado) {
    var update = new Update();
    update.set("usuario", usuarioAsignado.getUsuarioID().value());

    template.updateFirst(
        filtrarPorIdDeWallet(usuarioAsignado.aggregateRootId()), update,
        COLLECTION_VIEW).block();
  }

  @EventListener
  public void handleSaldoModificado(SaldoModificado saldoModificado) {
    var update = new Update();
    update.set("saldo", saldoModificado.getCantidad().value());

    template.updateFirst(
        filtrarPorIdDeWallet(saldoModificado.aggregateRootId()), update,
        COLLECTION_VIEW).block();
  }

  private Query filtrarPorIdDeWallet(String walletId) {
    return new Query(Criteria.where("_id").is(walletId));
  }
}