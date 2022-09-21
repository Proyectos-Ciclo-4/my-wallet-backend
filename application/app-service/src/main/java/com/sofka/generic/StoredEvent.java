package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.JsonParseException;
import java.util.Date;
import lombok.Data;

@Data
public class StoredEvent {

  private String eventBody;

  private Date occurredOn;

  private String typeName;

  public StoredEvent() {
  }

  public StoredEvent(String typeName, Date occurredOn, String eventBody) {
    this.typeName = typeName;
    this.occurredOn = occurredOn;
    this.eventBody = eventBody;
  }

  public DomainEvent deserializeEvent(EventSerializer eventSerializer) {
    try {
      return eventSerializer
          .deserialize(this.getEventBody(), Class.forName(this.getTypeName()));
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e.getMessage(), e.getCause());
    }
  }

  public static StoredEvent wrapEvent(DomainEvent domainEvent, EventSerializer eventSerializer) {
    return new StoredEvent(domainEvent.getClass().getCanonicalName(), new Date(),
        eventSerializer.serialize(domainEvent));
  }

  public interface EventSerializer {

    <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

    String serialize(DomainEvent object);
  }
}