import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptAcknowledgedMessage;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import yappl.YaPPL;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurposeInterceptor extends AbstractInterceptHandler {

    BrokerClient brokerClient = new BrokerClient();
    YaPPL policyHandler = new YaPPL();

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

        String policyId = brokerClient.getTopicPolicyIdForCustomer(msg.getClientID(), msg.getTopicName());
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
        brokerClient.updateTopicPolicy(customerId, topic, policyHandler.generateNewPolicyId());
    }
}
