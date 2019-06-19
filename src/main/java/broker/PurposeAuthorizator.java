package broker;

import io.moquette.spi.impl.subscriptions.Topic;
import io.moquette.spi.security.IAuthorizator;

public class PurposeAuthorizator implements IAuthorizator {

    @Override
    public boolean canWrite(Topic topic, String user, String client) {
        return !topic.toString().contains("purpose");

    }

    @Override
    public boolean canRead(Topic topic, String user, String client) {
        if (!topic.toString().contains("purpose")) {
            return true;
        }
        return client.equals(topic.toString().split("/")[1]);
    }
}
