package com.sofka.generic;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.generic.StoredEvent.EventSerializer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Blockchain {

  static OkHttpClient client = new OkHttpClient();
  private static URL POST_URL = new URL("http://localhost:8084/blockchain/wallet");

  private static URL GET_URL = new URL("http://localhost:8084/blockchain/wallet");
  @Autowired
  private static final EventSerializer serializer = null;

  public Blockchain(EventSerializer serializer) throws MalformedURLException {
    this.serializer = serializer;
  }

  
  
  //TODO verificar tokens en los header

  public static Response postEvent(DomainEvent event) throws IOException {

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

  public static Flux<TransferenciaBlockchain> getTransactionsFromID(String walletID){
      
    //IDrepo.getTransactionIDs(WalletID).collect().flatMapIterable(transactionID ---> {})
    
    var transactionID = "12345"; //Mock transaction ID
    
    Request request = new Request.Builder()
          .url(GET_URL + transactionID)
          .build();

    Call call = client.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) throws IOException {
          // ...
        }
        
        public void onFailure(Call call, IOException e) {
          System.out.println("Failed");
        }
      });
    
    //collectList()
    
    return null;
  }

}
