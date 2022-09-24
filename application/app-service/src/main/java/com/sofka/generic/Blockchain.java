package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Blockchain  {

  private static URL POST_URL;

  private static URL GET_URL;

  private static OkHttpClient client = new OkHttpClient();

  private static BlockchainTransactionIdRepository transactionIDrepo;

  @Autowired
  private static EventSerializer serializer;

  public Blockchain(EventSerializer serializer) throws MalformedURLException {
    Blockchain.serializer = serializer;
    POST_URL = new URL("");
    GET_URL = new URL("");
  }

  
  //TODO verificar tokens en los header

  public static Response postEventToBlockchain(DomainEvent event) throws IOException {

    RequestBody formBody = new FormBody.Builder()
        .add("event", serializer.serialize(event))
        .build();

    Request request = new Request.Builder()
        .url(POST_URL)
        .post(formBody)
        .build();

    Call call = client.newCall(request);
    return call.execute();
  }

  public static Response getFromBlockchain(String id) throws IOException{

    Request request = new Request.Builder()
        .url(GET_URL + id)
        .build();

    Call call = client.newCall(request);

    return call.execute();
  }

  //Esto no es reactivo y probablemente no es funcional... esperemos que funcione!
  public List<DomainEvent> getTransactionHistory(String walletID) throws IOException{

    List<DomainEvent> transacciones = new ArrayList<>();

    transactionIDrepo.getTransactionBlockchainsIDs(walletID)
        .toStream().collect(Collectors.toList()).forEach(Id -> {
          try {
            var response = getFromBlockchain(Id);
            var transaccion = serializer.deserialize(response.body().string(), DomainEvent.class);
            transacciones.add(transaccion);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    return transacciones;
  }

}
