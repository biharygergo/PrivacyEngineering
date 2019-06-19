package clients;

import org.apache.commons.lang3.RandomStringUtils;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class PublisherSyncClient {
    MQTT mqtt;
    BlockingConnection connection;

    public PublisherSyncClient(String host, int port) {
        mqtt = new MQTT();
        try {
            // mqtt.setHost("localhost", 1883);
            mqtt.setHost(host, port);
            mqtt.setHost("tcp://" + host + ":" + port);
            mqtt.setClientId(RandomStringUtils.randomAlphabetic(23));

            connection = mqtt.blockingConnection();
            connection.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String topic, String message) {
        try {
            connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
