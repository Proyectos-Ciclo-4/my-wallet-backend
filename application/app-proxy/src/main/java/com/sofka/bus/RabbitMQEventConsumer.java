package com.sofka.bus;


import com.sofka.ApplicationConfig;
import com.sofka.GsonEventSerializer;
import com.sofka.SocketController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventConsumer {

  private final GsonEventSerializer serializer;

  private final SocketController ws;

  public RabbitMQEventConsumer(GsonEventSerializer serializer, SocketController ws) {
    this.serializer = serializer;
    this.ws = ws;
  }

  @RabbitListener(queues = ApplicationConfig.GENERAL_QUEUE)
  public void receivedMessage(Message<String> message) {
    var notification = Notification.from(message.getPayload());
    try {
      var event = serializer.deserialize(notification.getBody(),
          Class.forName(notification.getType()));

      ws.send(event.aggregateRootId(), event);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @RabbitListener(queues = ApplicationConfig.REGISTER_QUEUE)
  public void receivedRegister(Message<String> message) {
    System.out.println("Received register: " + message.getPayload());
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

 /*public void receivedMessage(String received) {
    var event = serializer.deserialize(received, DomainEvent.class);
    ws.send(event.aggregateRootId(), event);
  }
*/

/*
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = ApplicationConfig.REGISTER_QUEUE, durable = "true"),
    exchange = @Exchange(value = ApplicationConfig.REGISTER_QUEUE, type = "topic")
))*/
