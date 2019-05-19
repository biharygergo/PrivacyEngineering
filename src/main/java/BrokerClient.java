import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.net.URISyntaxException;

public class BrokerClient {

    MQTT mqtt;

    public BrokerClient() {
        mqtt = new MQTT();
        try {
            mqtt.setHost("localhost", 1883);
            mqtt.setHost("tcp://localhost:1883");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            BlockingConnection connection = mqtt.blockingConnection();
            connection.connect();
            connection.publish("foo", "Hello".getBytes(), QoS.AT_LEAST_ONCE, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
