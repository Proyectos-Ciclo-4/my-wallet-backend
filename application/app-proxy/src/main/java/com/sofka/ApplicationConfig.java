package com.sofka;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class ApplicationConfig {

  public static final String GENERAL_QUEUE = "events.general";

  public static final String REGISTER_QUEUE = "events.proxy.register";

  public static final String REGISTER_EXCHANGE = "register";


  /*
  @Bean
  public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
    var admin = new RabbitAdmin(rabbitTemplate);
    admin.declareExchange(new TopicExchange(EXCHANGE));
    return admin;
  }
 */

  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}