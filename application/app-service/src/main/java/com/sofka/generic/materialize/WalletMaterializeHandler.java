package com.sofka.generic.materialize;

import com.mongodb.client.result.UpdateResult;
import com.sofka.domain.wallet.eventos.ContactoAgregado;
import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
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

  @EventListener
  public Mono<HashMap<Object, Object>> handleWalletCreada(WalletCreada walletCreada) {
    log.info("Materializing WalletCreada event: {}", walletCreada);
    var data = new HashMap<>();

    data.put("walletId", walletCreada.getWalletID().value());
    data.put("usuario", walletCreada.getUsuarioID().value());
    data.put("contactos", new ArrayList<>());
    data.put("historial", new ArrayList<>());
    data.put("motivos", new ArrayList<>(List.of(new Motivo("Desconocido", "#CBCBCB"))));
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
        COLLECTION_VIEW).flatMap(updateResult -> template.save(usuario, "usuarios"));
  }

  @EventListener
  public Mono<UpdateResult> handleContactoAgregado(ContactoAgregado contactoAgregado) {
    log.info("Materializing ContactoAgregado event: {}", contactoAgregado);
    var update = new Update();
    var query = new Query(Criteria.where("walletId").is(contactoAgregado.getWalletID().value()));
    var contacto = new HashMap<String, String>();
    var value = contactoAgregado.getContacto();

    contacto.put("walletId", value.identity().value());
    contacto.put("nombre", value.getNombre().value());
    contacto.put("telefono", value.getTelefono().value());
    contacto.put("email", value.getEmail().value());

    update.addToSet("contactos", contacto);

    return template.updateFirst(query, update, COLLECTION_VIEW);
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