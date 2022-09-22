package com.sofka;


import co.com.sofka.domain.generic.DomainEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/wallet/{correlationId}")
public class SocketController {

  private static final Logger logger = Logger.getLogger(SocketController.class.getName());
  private static Map<String, Map<String, Session>> sessions;
  @Autowired
  private GsonEventSerializer serialize;

  public SocketController() {
    if (Objects.isNull(sessions)) {
      sessions = new ConcurrentHashMap<>();
    }
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("correlationId") String correlationId) {
    logger.info("Connect by " + correlationId);
    var map = sessions.getOrDefault(correlationId, new HashMap<>());
    logger.info(sessions.containsKey(correlationId) ? "Contains" : "Not contains");
    logger.info(sessions.keySet().toString());
    map.put(session.getId(), session);
    sessions.put(correlationId, map);
  }

  @OnClose
  public void onClose(Session session, @PathParam("correlationId") String correlationId) {
    sessions.get(correlationId).remove(session.getId());
    logger.info("Desconnect by " + correlationId);
  }

  @OnError
  public void onError(Session session, @PathParam("correlationId") String correlationId,
      Throwable throwable) {
    sessions.get(correlationId).remove(session.getId());
    logger.log(Level.SEVERE, throwable.getMessage());

  }

  public void send(String correlationId, DomainEvent event) {

    var message = serialize.serialize(event);
    if (Objects.nonNull(correlationId) && sessions.containsKey(correlationId)) {
      logger.info("send for" + correlationId);

      sessions.get(correlationId).values().parallelStream()
          .forEach(session -> {
            try {
              session.getBasicRemote().sendText(message);
            } catch (RuntimeException | IOException e) {
              logger.log(Level.SEVERE, e.getMessage(), e);
            }
          });
    }
  }

}
