package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.sofka.generic.StoredEvent.EventSerializer;
import com.sofka.generic.materialize.model.SavedHash;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    var storedEvent = StoredEvent.wrapEvent(event, serializer);
    var body = new Gson().toJson(storedEvent);

    log.info("Sending to blockchain {}", body);

    var request = new Request.Builder().url(POST_URL)
        .post(okhttp3.RequestBody.create(body, MEDIA_TYPE_JSON))
        .addHeader("Authorization", String.format("Bearer %s", APP_TOKEN)).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      var hashBody = response.body().string().split(":")[1].replace("\"", "").replace("}", "");

      var hashToSave = new SavedHash(hashBody, event.getClass().getTypeName());

      repository.saveEventHash(hashToSave)
          .subscribe(savedHash -> log.info("saved hash: " + savedHash.getHash()));
    }
  }

  public void getTransactionHistory(String walletID) {
    Request request = new Request.Builder().url(GET_URL).build();

    client.newCall(request).enqueue(callback);
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