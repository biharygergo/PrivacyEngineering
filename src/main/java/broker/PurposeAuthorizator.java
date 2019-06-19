package broker;

import io.moquette.spi.impl.subscriptions.Topic;
import io.moquette.spi.security.IAuthorizator;

public class PurposeAuthorizator implements IAuthorizator {

    @Override
    public boolean canWrite(Topic topic, String user, String client) {
        return !topic.toString().contains("purpose") || client.contains("4c697849-34d8-4159-879f-");
    }

    @Override
    public boolean canRead(Topic topic, String user, String client) {
        if (client.equals("benchmarker") || client.contains("4c697849-34d8-4159-879f-")) {
            return true;
        }
        if (topic.toString().contains("purpose") &&
                client.equals(topic.toString().split("/")[1])) {
            return true;
        }
        return false;
    }
}
