package com.sofka.adapters.repositories;

import com.sofka.generic.StoredEvent;
import lombok.Data;

@Data
public class DocumentEventStored {

  private String aggregateRootId;

  private StoredEvent storedEvent;
}