package broker;

public interface OnRepublicationEventListener {
    void republish(String topic, String message);
}
