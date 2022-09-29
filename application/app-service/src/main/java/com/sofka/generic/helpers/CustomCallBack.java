package com.sofka.generic.helpers;

import com.sofka.generic.EventBus;
import com.sofka.generic.StoredEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import com.sofka.generic.materialize.model.TransaccionDeHistorial;
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

    var event = eventSerializer.deserialize(body, TransaccionDeHistorial.class);

    eventBus.publish(event);

    log.info("body: " + body);
  }
}