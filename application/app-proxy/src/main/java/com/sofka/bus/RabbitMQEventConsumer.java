package com.sofka.bus;


import com.sofka.ApplicationConfig;
import com.sofka.GsonEventSerializer;
import com.sofka.SocketController;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
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


  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = "proxy.handles", durable = "true"),
      exchange = @Exchange(value = ApplicationConfig.EXCHANGE, type = "topic"),
      key = "example.#"
  ))
  public void receivedMessage(Message<String> message) {
    var notification = Notification.from(message.getPayload());
    try {
      var event = serializer.deserialize(
          notification.getBody(), Class.forName(notification.getType())
      );
      ws.send(event.aggregateRootId(), event);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }


}
