package clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientSimulator extends Thread {

    public void run() {
        for (int i = 0; i < 50; i++) {
            PublisherSyncClient client = new PublisherSyncClient("localhost", 1883);
            try {
                client
                        .sendMessage(
                                "temperature",
                                new ObjectMapper().writeValueAsString(VattenfallMessage.createFakeMessage(
                                        client.getClientId(),
                                        "temperature")));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
