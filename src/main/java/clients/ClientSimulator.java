package clients;

public class ClientSimulator extends Thread {

    public void run() {
        for (int i = 0; i < 50; i++) {
            PublisherSyncClient client = new PublisherSyncClient("localhost", 1883);
            client.sendMessage("temperature", "Hello");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
