package broker;

import broker.helpers.ObjectFieldHelper;
import broker.parsers.Purpose;
import broker.parsers.PurposeParser;
import clients.VattenfallMessage;
import clients.VattenfallPurposeMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private List<Purpose> parsedPurposes = new PurposeParser().getPurposesFromConfig();
    private ObjectMapper objectMapper = new ObjectMapper();
    private PurposeInterceptor interceptor = new PurposeInterceptor();

    public void startBroker() throws IOException {
        IResourceLoader classpathLoader = new ClasspathResourceLoader();
        final IConfig classPathConfig = new ResourceLoaderConfig(classpathLoader);

        final Server mqttBroker = new Server();

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

    /**
     * Republishes a message into the purpose topology. Retrieves the message topic and tries to match it to an existing
     * Policy. Then, creates a new instance of a VatenfallPurposeMessage, transforms the message based on the policy
     * and republishes the message into the purpose topology.
     * @param topic a message was published to
     * @param message message payload (data)
     * @param mqttBroker broker instance
     */
    private void republishMessage(String topic, String message, Server mqttBroker) {
        try {
            // Parse the message that is to be republished
            VattenfallMessage vattenfallMessage = objectMapper.readValue(message, VattenfallMessage.class);
            Purpose purpose = parsedPurposes.stream().filter(purpose1 ->
                    purpose1.getId().equals(topic.substring(topic.indexOf("/") + 1))).findFirst().orElse(null);
            if (purpose != null) {
                // If the topic is known, republish according to its rules.

                VattenfallPurposeMessage vattenfallPurposeMessage = new VattenfallPurposeMessage();

                // Add the properties that should be included in the purpose topology.
                addAdditionalProperties(vattenfallMessage, purpose, vattenfallPurposeMessage);

                // Remove properties that should not be present in purpose topology first.
                removeProperties(vattenfallMessage, purpose);

                vattenfallPurposeMessage.setOriginalMessage(vattenfallMessage);

                // Republish this message in the purpose topology
                publishMessageInternally(topic, mqttBroker, vattenfallPurposeMessage);
            } else {
                System.out.println("Purpose not found! \n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Message could not be parsed as VattenfallMessage.");
        }
    }

    /**
     * Appends additional properties to a message, as specified in the purpose
     * @param vattenfallMessage original message
     * @param purpose Purpose specified for the given topic by a client that sent the message
     * @param vattenfallPurposeMessage transformed vatenfallMessage containing desired fields.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void addAdditionalProperties(VattenfallMessage vattenfallMessage, Purpose purpose, VattenfallPurposeMessage vattenfallPurposeMessage) throws NoSuchFieldException, IllegalAccessException {
        if (!purpose.getAddedProperties().isEmpty()) {
            Customer customer = interceptor.getCustomers().stream()
                    .filter(customer1 -> customer1.getId().equals(vattenfallMessage.getSubscriber_id())).findFirst().orElse(null);

            if (customer != null) {
                for (String toAdd : purpose.getAddedProperties()) {
                    if (toAdd.equals("offer_type")) {
                        // TODO implement offer type property here
                    } else {
                        String newProperty = ObjectFieldHelper.getProperty(customer, toAdd);
                        ObjectFieldHelper.setProperty(vattenfallPurposeMessage, toAdd, newProperty);
                    }


                }
            } else {
                System.out.println("Customer not found! \n");
            }
        }
    }

    /**
     * Removes additional properties from vatenfallMessage, as specified in the purpose
     * @param vattenfallMessage message to be transformed
     * @param purpose Purpose based on which the message will be transformed
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void removeProperties(VattenfallMessage vattenfallMessage, Purpose purpose) throws NoSuchFieldException, IllegalAccessException {
        for (String toRemove : purpose.getRemovedProperties()) {
            ObjectFieldHelper.setProperty(vattenfallMessage, toRemove, "");
        }
    }

    /**
     * Publishes the vatenfallPurposeMessage into the purpose topology
     * @param topic purpose topic for which a message will be published
     * @param mqttBroker instance of the broker
     * @param vattenfallPurposeMessage message to be published
     * @throws JsonProcessingException
     */
    private void publishMessageInternally(String topic, Server mqttBroker, VattenfallPurposeMessage vattenfallPurposeMessage) throws JsonProcessingException {
        MqttPublishMessage messageToSend = MqttMessageBuilders.publish()
                .topicName("purpose-topology/" + topic)
                .retained(true)
                .qos(MqttQoS.EXACTLY_ONCE)
                .payload(Unpooled.copiedBuffer(objectMapper.writeValueAsString(vattenfallPurposeMessage).getBytes(UTF_8)))
                .build();

        mqttBroker.internalPublish(messageToSend, "INTRLPUB");
    }

}
