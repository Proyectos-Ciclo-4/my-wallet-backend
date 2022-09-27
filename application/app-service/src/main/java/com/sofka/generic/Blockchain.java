package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Blockchain {

  private final EventStoreRepository repository;

  private static URL POST_URL;

  static {
    try {
      POST_URL = new URL("http://localhost:8090/api/collections/blockchain/records");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private static URL GET_URL;

  static {
    try {
      GET_URL = new URL("http://localhost:8090/api/collections/blockchain/records/2hrwvpz8a9zp22v");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private static OkHttpClient client = new OkHttpClient();

  public static final MediaType MEDIA_TYPE_JSON = MediaType.parse(
      "application/json; charset=utf-8");

  private static BlockchainTransactionIdRepository transactionIDrepo;

  @Autowired
  private static EventSerializer serializer;

  public Blockchain(EventStoreRepository repository, EventSerializer serializer) {
    this.repository = repository;
    Blockchain.serializer = serializer;

  }

  //TODO verificar tokens en los header

  public void postEventToBlockchain(DomainEvent event) {
    try {
      run(event);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void run(DomainEvent event) throws Exception {
    var storedEvent = StoredEvent.wrapEvent(event, serializer);
    var body = new Gson().toJson(storedEvent);

    log.warn("body: " + body);

    var request = new Request.Builder().url(POST_URL)
        .post(okhttp3.RequestBody.create(body, MEDIA_TYPE_JSON)).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      log.info("Blockchain response {}", response.body().string());
    }

  }

  public Response getFromBlockchain(String id) {

    Request request = new Request.Builder().url(GET_URL + id).build();

    Call call = client.newCall(request);

    try {
      return call.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //Esto no es reactivo y probablemente no es funcional... esperemos que funcione!
  public List<DomainEvent> getTransactionHistory(String walletID) throws IOException {

    List<DomainEvent> transacciones = new ArrayList<>();

    transactionIDrepo.getTransactionBlockchainsIDs(walletID).toStream().collect(Collectors.toList())
        .forEach(Id -> {
          try {
            var response = getFromBlockchain(Id);
            var transaccion = serializer.deserialize(response.body().string(), DomainEvent.class);
            transacciones.add(transaccion);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    return transacciones;
  }

}
