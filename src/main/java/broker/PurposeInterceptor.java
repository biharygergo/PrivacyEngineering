package broker;

import broker.parsers.PurposeParser;
import com.github.javafaker.Faker;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import yappl.models.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurposeInterceptor extends AbstractInterceptHandler {

    private ArrayList<Customer> customers = new ArrayList<>();
    private Map<String, Map<String, String>> customerIdToTopicPolicyMapping = new HashMap<>();
    private PolicyHandler policyHandler = new PolicyHandler();
    private Faker faker = new Faker();
    private PurposeParser parser = new PurposeParser();

    private OnRepublicationEventListener onRepublicationEventListener;

    @Override
    public void onConnect(InterceptConnectMessage msg) {
        String clientID = msg.getClientID();
        System.out.println("Connected - " + clientID);

        createNewCustomer(clientID);

        List<String> topics = parser.getAvailableGeneralTopicIds();
        for (String topic : topics) {
            this.updateTopicPolicyMapping(clientID, topic, String.valueOf(policyHandler.createFakePolicy().getId()));
        }


    }


    @Override
    public String getID() {
        return "EmbeddedLauncherPublishListener";
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {
        // Do not republish messages that are sent on purpose topology
        if (msg.getTopicName().contains("purpose-topology")) {
            return;
        }

        final String decodedPayload = convertPayloadToBytes(msg);
       /* System.out.println(
                "Received on topic: " + msg.getTopicName() +
                        " content: " + decodedPayload +
                        " from: " + msg.getClientID());*/

        String policyId = customerIdToTopicPolicyMapping.get(msg.getClientID()).get(msg.getTopicName());

        if (policyId != null) {
            Policy policy = policyHandler.findPolicyById(Integer.parseInt(policyId));
            for (String topic : policyHandler.getAllowedRepublicationTopics(policy)) {
                if (onRepublicationEventListener != null) {
                    onRepublicationEventListener.republish(topic, decodedPayload);
                }
            }

        }

    }

    private String convertPayloadToBytes(InterceptPublishMessage msg) {
        byte[] bytes = new byte[msg.getPayload().readableBytes()];
        msg.getPayload().duplicate().readBytes(bytes);
        return new String(bytes, UTF_8);
    }


    private void createNewCustomer(String clientID) {
        customers.add(
                new Customer(
                        clientID,
                        faker.funnyName().name(),
                        faker.address().fullAddress(),
                        faker.phoneNumber().phoneNumber()
                )
        );
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


    public OnRepublicationEventListener getOnRepublicationEventListener() {
        return onRepublicationEventListener;
    }

    void setOnRepublicationEventListener(OnRepublicationEventListener onRepublicationEventListener) {
        this.onRepublicationEventListener = onRepublicationEventListener;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}

