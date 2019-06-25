package clients;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class VattenfallMessage {
    private String subscriberId;
    private String measurementType;
    private double measurement;
    private long timestamp = Instant.now().getEpochSecond();

    static VattenfallMessage createFakeMessage(String subscriberId, String topic) {
        VattenfallMessage message = new VattenfallMessage();
        message.setSubscriberId(subscriberId);
        message.setMeasurementType(topic);
        message.measurement = ThreadLocalRandom.current().nextDouble(0, 100);
        return message;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
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
