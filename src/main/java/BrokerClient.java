import org.apache.commons.lang3.RandomStringUtils;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class BrokerClient {

    MQTT mqtt;
    CallbackConnection connection;

    public BrokerClient(String host, int port) {
        mqtt = new MQTT();
        try {
            // mqtt.setHost("localhost", 1883);
            mqtt.setHost(host, port);
            mqtt.setHost("tcp://" + host + ":" + port);
            //System.out.println("GIVEN_ID_" + Math.random());
            //mqtt.setClientId("GIVEN_ID_" + Math.random());
            //System.out.println(mqtt.getClientId());
            mqtt.setClientId(RandomStringUtils.randomAlphabetic(23));

            connection = mqtt.callbackConnection();
            connection.connect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String topic, String message) {
        try {

            connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
