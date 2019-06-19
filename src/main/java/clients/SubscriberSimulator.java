package clients;

import broker.parsers.UtilizerParser;

import java.util.List;

public class SubscriberSimulator extends Thread {

    public void run() {
        List<String> utilizers = new UtilizerParser().getAvailableUtilizerIds();
        System.out.println("UTILIZER: " + utilizers.get(0));
        SubscriberClient subscriberClient = new SubscriberClient("localhost", 1883, utilizers.get(1),
                "purpose-topology/" + utilizers.get(0) + "/#");
    }
}
