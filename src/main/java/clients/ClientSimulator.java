package clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientSimulator extends Thread {

    /**
     * Starts the clientSimulator.
     */
    public void run() {
        for (int i = 0; i < 10; i++) {
            PublisherSyncClient client = new PublisherSyncClient("localhost", 1883);
            sendMessageWithClient(client, "temperature");
            sendMessageWithClient(client, "water");
            sendMessageWithClient(client, "light");
            sendMessageWithClient(client, "temperature");
            sendMessageWithClient(client, "water");
        }
    }

    private void sendMessageWithClient(PublisherSyncClient client, String topic) {
        System.out.println("Sending message to: " + topic + " - from: " + client.getClientId());
        try {
            client
                    .sendMessage(
                            topic,
                            new ObjectMapper().writeValueAsString(VattenfallMessage.createFakeMessage(
                                    client.getClientId(),
                                    topic)));
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
