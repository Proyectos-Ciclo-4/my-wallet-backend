package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.sofka.adapters.repositories.DocumentEventStored;
import com.sofka.domain.wallet.eventos.HistorialRecuperado;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.generic.StoredEvent.EventSerializer;
import com.sofka.generic.helpers.EncrypDES3;
import com.sofka.generic.materialize.model.SavedHash;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class Blockchain {

  private final Callback callback;

  private final EventStoreRepository repository;

  private URL POST_URL;

  private URL GET_URL;

  private final String APP_TOKEN;

  private static final OkHttpClient client = new OkHttpClient();

  public static final MediaType MEDIA_TYPE_JSON = MediaType.parse(
      "application/json; charset=utf-8");

  private static EventSerializer serializer;

  public Blockchain(EventStoreRepository repository, EventSerializer serializer,
      @Value("${blockchain.post.url}") String post, @Value("${blockchain.get.url}") String get,
      Callback callback, @Value("${blockchain.token}") String app_token) {
    this.repository = repository;
    Blockchain.serializer = serializer;
    this.callback = callback;
    APP_TOKEN = app_token;
    buildUrls(post, get);
  }

  public void postEventToBlockchain(DomainEvent event) {
    try {
      run(event);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void run(DomainEvent event) throws Exception {
    var evento = getEventoDeHistorial(event);
    var storedEvent = StoredEvent.wrapEvent(evento, serializer);
    var documentEventStored = new DocumentEventStored();
    documentEventStored.setAggregateRootId(evento.aggregateRootId());
    documentEventStored.setStoredEvent(storedEvent);

    var body = new Gson().toJson(documentEventStored);

    body = encryptBody(body);

    log.info("Sending to blockchain {}", body);

    var request = new Request.Builder().url(POST_URL)
        .post(okhttp3.RequestBody.create(body, MEDIA_TYPE_JSON))
        .addHeader("Authorization", String.format("Bearer %s", APP_TOKEN)).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      var hashBody = response.body().string().split(":")[1].replace("\"", "").replace("}", "");

      var hashToSave = new SavedHash(hashBody, evento.getClass().getTypeName(),
          event.aggregateRootId());

      repository.saveEventHash(hashToSave)
          .subscribe(savedHash -> log.info("saved hash: " + savedHash.getHash()));
    }
  }

  private String encryptBody(String body) {
    try {
      EncrypDES3 encrypDES3 = new EncrypDES3();
      return Arrays.toString(encrypDES3.Encrytor(body));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
             BadPaddingException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  private static DomainEvent getEventoDeHistorial(DomainEvent event) {
    if (event instanceof TransferenciaExitosa) {
      var historialRecuperado = new HistorialRecuperado((TransferenciaExitosa) event);
      historialRecuperado.setAggregateRootId(event.aggregateRootId());

      return historialRecuperado;
    }

    return event;
  }

  public void getTransactionHistory(Flux<SavedHash> hash) {
    hash.doOnNext(savedHash -> {
      var url = GET_URL.toString() + savedHash.getHash();
      Request request = new Request.Builder().url(url).build();

      client.newCall(request).enqueue(callback);
    }).subscribe(savedHash -> log.info("Getting history: " + savedHash.getHash()));
  }

  private void buildUrls(String postUrl, String getUrl) {
    try {
      POST_URL = new URL(postUrl);
      GET_URL = new URL(getUrl);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}