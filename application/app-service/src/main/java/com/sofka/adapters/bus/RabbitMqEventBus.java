package com.sofka.adapters.bus;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.sofka.ApplicationConfig;
import com.sofka.generic.EventBus;
import com.sofka.generic.StoredEvent.EventSerializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventBus implements EventBus {

  private final RabbitTemplate rabbitTemplate;
  private final EventSerializer serializer;
  private final Gson gson = new Gson();

  public RabbitMqEventBus(RabbitTemplate rabbitTemplate, EventSerializer eventSerializer) {
    this.rabbitTemplate = rabbitTemplate;
    this.serializer = eventSerializer;
  }

  @Override
  public void publish(DomainEvent event) {
    rabbitTemplate.convertAndSend(
        ApplicationConfig.EXCHANGE, ApplicationConfig.GENERAL_ROUTING_KEY,
        serializer.serialize(event)
    );
  }

  /*
  @Override
  public void publishError(Throwable errorEvent) {
        rabbitTemplate.convertAndSend(ApplicationConfig.EXCHANGE, ApplicationConfig.GENERAL_ROUTING_KEY,
        serializer.;
  }
  */

}
