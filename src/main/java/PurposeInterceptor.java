import com.github.javafaker.Faker;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptAcknowledgedMessage;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import yappl.YaPPL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurposeInterceptor extends AbstractInterceptHandler {

    ArrayList<Customer> customers = new ArrayList<>();
    Map<String, Map<String, String>> customerIdToTopicPolicyMapping = new HashMap<>();
    YaPPL policyHandler = new YaPPL();
    Faker faker = new Faker();

    @Override
    public void onConnect(InterceptConnectMessage msg) {
        System.out.println("Connect");
    }

    @Override
    public String getID() {
        return "EmbeddedLauncherPublishListener";
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {
        final String decodedPayload = convertPayloadToBytes(msg);
        System.out.println("Received on topic: " + msg.getTopicName() + " content: " + decodedPayload);

        String policyId = customerIdToTopicPolicyMapping.get(msg.getClientID()).get(msg.getTopicName());
        if (policyId != null) {
            // policyHandler.getPolicy(policyId)
            // For each purpose
            // If allowed in policy, republish
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

        String customerId = msg.getClientID();
        String topic = msg.getTopicFilter();
        customers.add(
            new Customer(
                customerId,
                faker.funnyName().toString(),
                faker.address().toString(),
                faker.phoneNumber().toString()
            )
        );

        this.updateTopicPolicyMapping(customerId, topic, policyHandler.generateNewPolicyId());
    }

    private void updateTopicPolicyMapping(String customerId, String topic, String policyId) {
        Map<String, String> tempMap = new HashMap<String, String>() {{ put(topic, policyId); }};
        customerIdToTopicPolicyMapping.put(customerId, tempMap);
    }
}
