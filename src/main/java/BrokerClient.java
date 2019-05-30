import com.google.common.collect.Maps;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrokerClient {

    MQTT mqtt;
    Map<String, Map<String, String>> customerIdToTopicPolicyMapping;

    public BrokerClient() {
        mqtt = new MQTT();
        customerIdToTopicPolicyMapping = new HashMap<>();

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

    public void updateTopicPolicy(String customerId, String topic, String policyId) {
        Map<String, String> tempMap = new HashMap<>();

        // policyId/topic validation?
        tempMap.put(topic, policyId);
        customerIdToTopicPolicyMapping.put(customerId, tempMap);
    }


}
