package broker;

import io.moquette.interception.InterceptHandler;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathResourceLoader;
import io.moquette.server.config.IConfig;
import io.moquette.server.config.IResourceLoader;
import io.moquette.server.config.ResourceLoaderConfig;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurposeBroker {

    public void startBroker() throws IOException {
        IResourceLoader classpathLoader = new ClasspathResourceLoader();
        final IConfig classPathConfig = new ResourceLoaderConfig(classpathLoader);

        final Server mqttBroker = new Server();

        PurposeInterceptor interceptor = new PurposeInterceptor();
        List<? extends InterceptHandler> userHandlers = Collections.singletonList(interceptor);

        // Start the broker
        mqttBroker.startServer(classPathConfig, userHandlers, null, null, new PurposeAuthorizator());
        System.out.println("Broker started. Press [CTRL+C] to stop.");

        //Bind a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping broker");
            mqttBroker.stopServer();
            System.out.println("Broker stopped");
        }));

        interceptor.setOnRepublicationEventListener((topic, message) -> republishMessage(topic, message, mqttBroker));
    }

    private void republishMessage(String topic, String message, Server mqttBroker) {
        MqttPublishMessage messageToSend = MqttMessageBuilders.publish()
                .topicName(topic)
                .retained(true)
                .qos(MqttQoS.EXACTLY_ONCE)
                .payload(Unpooled.copiedBuffer(message.getBytes(UTF_8)))
                .build();

        mqttBroker.internalPublish(messageToSend, "INTRLPUB");
    }
}
