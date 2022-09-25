package com.sofka.bus;


import com.sofka.GsonEventSerializer;
import com.sofka.SocketController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQEventConsumer {

  private final GsonEventSerializer serializer;

  private final SocketController ws;

  public RabbitMQEventConsumer(GsonEventSerializer serializer, SocketController ws) {
    this.serializer = serializer;
    this.ws = ws;
  }

  @RabbitListener(queues = {"${wallet.general.queue.name}"})
  public void receivedMessage(@Payload Message<String> message) {
    log.info("Received message from general queue: {}", message.getPayload());

    var notification = Notification.from(message.getPayload());
    try {
      var event = serializer.deserialize(notification.getBody(),
          Class.forName(notification.getType()));

      ws.send(event.aggregateRootId(), event);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @RabbitListener(queues = {"${wallet.register.queue.name}"})
  public void receivedRegister(@Payload Message<String> message) {
    log.info("Received message from register queue: {}", message.getPayload());

    var notification = Notification.from(message.getPayload());

    try {
      var event = serializer.deserialize(notification.getBody(),
          Class.forName(notification.getType()));

      var notificationBody = notification.getBody();

      var userId = notificationBody.split(":")[2].split("}")[0].replaceAll("\"", "");

      ws.send(userId, event);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}