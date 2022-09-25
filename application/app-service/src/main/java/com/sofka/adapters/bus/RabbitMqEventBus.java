package com.sofka.adapters.bus;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.EventBus;
import com.sofka.generic.StoredEvent.EventSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@Slf4j
public class RabbitMqEventBus implements EventBus {

  private final RabbitTemplate rabbitTemplate;

//  private final ApplicationEventPublisher eventPublisher;


  private final Queue generalQueue;


  private final Queue registerQueue;

  private final EventSerializer serializer;

  public RabbitMqEventBus(RabbitTemplate rabbitTemplate, ApplicationEventPublisher eventPublisher,
      @Qualifier("walletGeneralQueue") Queue generalQueue,
      @Qualifier("walletRegisterQueue") Queue registerQueue, EventSerializer eventSerializer) {

    this.rabbitTemplate = rabbitTemplate;
//    this.eventPublisher = eventPublisher;
    this.generalQueue = generalQueue;
    this.registerQueue = registerQueue;
    this.serializer = eventSerializer;
  }

  @Override
  public void publish(DomainEvent event) {
    log.info("Publishing event from general: {}", event);
    Notification notification = new Notification(event.getClass().getTypeName(),
        serializer.serialize(event));

    rabbitTemplate.convertAndSend(generalQueue.getName(), notification.serialize().getBytes());

//    eventPublisher.publishEvent(event);
  }

  @Override
  public void publishRegister(DomainEvent event) {
    log.info("Publishing event from register: {}", event.getClass().getTypeName());

    Notification notification = new Notification(event.getClass().getTypeName(),
        serializer.serialize(event));

    rabbitTemplate.convertAndSend(registerQueue.getName(), notification.serialize().getBytes());

//    eventPublisher.publishEvent(event);
  }
  /*
  @Override
  public void publishError(Throwable errorEvent) {
        rabbitTemplate.convertAndSend(ApplicationConfig.EXCHANGE, ApplicationConfig.GENERAL_ROUTING_KEY,
        serializer.;
  }
  */

}
