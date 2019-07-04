package clients;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

/**
 * A Subscriber client that can subscribe to a given topic on a given host on mqtt.
 */
public class SubscriberClient {

    MQTT mqtt;
    CallbackConnection connection;

    public SubscriberClient(String host, int port, String utilizerId, String topic) {
        mqtt = new MQTT();
        try {
            // mqtt.setHost("localhost", 1883);
            mqtt.setHost(host, port);
            mqtt.setHost("tcp://" + host + ":" + port);
            mqtt.setClientId(utilizerId);

            connection = mqtt.callbackConnection();
            connection.connect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {

                    subscribeToTopic(topic);
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });
            connection.listener(new Listener() {
                @Override
                public void onConnected() {

                }

                @Override
                public void onDisconnected() {

                }

                @Override
                public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                    // System.out.println("New message on subscription: " + new String(topic.toByteArray()) + "\n");
                    // System.out.println(new String(body.toByteArray(), UTF_8));
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(String topic) {
        Topic mqttTopic = new Topic(topic, QoS.AT_LEAST_ONCE);
        connection.subscribe(new Topic[]{mqttTopic}, new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] value) {
                System.out.println("SUBSCRIPTION SUCCESS");
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("SUBSCRIPTION FAILURE");

            }
        });

    }
}
