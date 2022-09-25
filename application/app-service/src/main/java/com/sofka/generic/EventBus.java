package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;

public interface EventBus {

  void publish(DomainEvent event);

  void publishRegister(DomainEvent event);

  /*
  void publishError(Throwable errorEvent);
   */

}
