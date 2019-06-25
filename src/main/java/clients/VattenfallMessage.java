package clients;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VattenfallMessage {
    @JsonProperty("subscriber_id")
    private String subscriber_id;
    @JsonProperty("measurement_type")
    private String measurement_type;
    private double measurement;
    private long timestamp = Instant.now().getEpochSecond();

    static VattenfallMessage createFakeMessage(String subscriberId, String topic) {
        VattenfallMessage message = new VattenfallMessage();
        message.setSubscriber_id(subscriberId);
        message.setMeasurement_type(topic);
        message.measurement = ThreadLocalRandom.current().nextDouble(0, 100);
        return message;
    }

    public String getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(String subscriber_id) {
        this.subscriber_id = subscriber_id;
    }

    public String getMeasurement_type() {
        return measurement_type;
    }

    public void setMeasurement_type(String measurement_type) {
        this.measurement_type = measurement_type;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
