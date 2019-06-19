package broker;

import broker.parsers.PurposeParser;
import clients.PublisherClient;
import clients.PublisherSyncClient;
import com.github.javafaker.Faker;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptAcknowledgedMessage;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import yappl.models.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurposeBroker extends AbstractInterceptHandler {

    ArrayList<Customer> customers = new ArrayList<>();
    Map<String, Map<String, String>> customerIdToTopicPolicyMapping = new HashMap<>();
    PolicyHandler policyHandler = new PolicyHandler();
    Faker faker = new Faker();

    PublisherSyncClient client;

    @Override
    public void onConnect(InterceptConnectMessage msg) {
        System.out.println("Connect");
        String customerId = msg.getClientID();
        customers.add(
                new Customer(
                        customerId,
                        faker.funnyName().toString(),
                        faker.address().toString(),
                        faker.phoneNumber().toString()
                )
        );

        PurposeParser parser = new PurposeParser();
        List<String> topics = parser.getAvailableGeneralTopicIds();
        for (String topic : topics) {
            this.updateTopicPolicyMapping(customerId, topic, String.valueOf(policyHandler.createFakePolicy().getId()));
        }


    }

    @Override
    public String getID() {
        return "EmbeddedLauncherPublishListener";
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {
        final String decodedPayload = convertPayloadToBytes(msg);
        System.out.println("Received on topic: " + msg.getTopicName() + " content: " + decodedPayload + " from: " + msg.getClientID());
        String policyId = customerIdToTopicPolicyMapping.get(msg.getClientID()).get(msg.getTopicName());
        List<String> topics = new PurposeParser().getAvailablePurposeTopicIds();

        if (topics.contains(msg.getTopicName())) {
            // System.out.println("Intercepted message on purpose graph...");
            return;
        }
        if (policyId != null) {
            Policy policy = policyHandler.findPolicyById(Integer.parseInt(policyId));

            for (String topic : policyHandler.getAllowedRepublicationTopics(policy)) {
                System.out.println(topic);
                client.sendMessage("purpose-topology/" + topic, decodedPayload);
                System.out.println("Republished on topic: " + topic);
            }

        }

    }

    private String convertPayloadToBytes(InterceptPublishMessage msg) {
        byte[] bytes = new byte[msg.getPayload().readableBytes()];
        msg.getPayload().duplicate().readBytes(bytes);
        return new String(bytes, UTF_8);
    }

    @Override
    public void onMessageAcknowledged(InterceptAcknowledgedMessage msg) {
        super.onMessageAcknowledged(msg);
        System.out.println("Message acknowledged.");
    }

    @Override
    public void onSubscribe(InterceptSubscribeMessage msg) {
        super.onSubscribe(msg);
    }

    private void updateTopicPolicyMapping(String customerId, String topic, String policyId) {
        Map<String, String> savedMap = customerIdToTopicPolicyMapping.get(customerId);
        Map<String, String> tempMap;
        if (savedMap != null) {
            savedMap.put(topic, policyId);
        } else {
            tempMap = new HashMap<String, String>() {{
                put(topic, policyId);
            }};
            customerIdToTopicPolicyMapping.put(customerId, tempMap);
        }
    }

    public PublisherSyncClient getClient() {
        return client;
    }

    public void setClient(PublisherSyncClient client) {
        this.client = client;
    }
}
