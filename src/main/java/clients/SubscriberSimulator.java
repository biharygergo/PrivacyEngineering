package clients;

import broker.parsers.UtilizerParser;

import java.util.List;

/**
 * A simulator used to start subscribers on a given topic.
 */
public class SubscriberSimulator extends Thread {

    public void run() {
        List<String> utilizers = new UtilizerParser().getAvailableUtilizerIds();
        System.out.println("UTILIZER: " + utilizers.get(0));
        SubscriberClient subscriberClient = new SubscriberClient("localhost", 1883, utilizers.get(0),
                "purpose-topology/" + utilizers.get(0) + "/#");
    }
}
