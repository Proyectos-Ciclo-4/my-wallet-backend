package com.sofka.generic.helpers;

import com.google.gson.Gson;
import com.sofka.adapters.repositories.DocumentEventStored;
import com.sofka.generic.EventBus;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class CustomCallBack implements Callback {

  private final EventBus eventBus;

  private final StoredEvent.EventSerializer eventSerializer;

  public CustomCallBack(EventBus eventBus, EventSerializer eventSerializer) {
    this.eventBus = eventBus;
    this.eventSerializer = eventSerializer;
  }

  @Override
  public void onFailure(@NotNull Call call, @NotNull IOException e) {
    log.error(e.getMessage());
  }

  @Override
  public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
    if (!response.isSuccessful()) {
      throw new IOException("Unexpected code " + response);
    }

    var body = response.body().string();

    log.info("Getting history: " + body);

    var documentEventStored = new Gson().fromJson(body, DocumentEventStored.class);

    var event = documentEventStored.getStoredEvent().deserializeEvent(eventSerializer);

    eventBus.publish(event);
  }
}