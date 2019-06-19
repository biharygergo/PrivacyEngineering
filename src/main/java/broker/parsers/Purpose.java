package broker.parsers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Purpose {
    @JsonProperty("id")
    String id = "";

    @JsonProperty("all_props")
    String allProps = "false";

    @JsonProperty("topic_name")
    String topicName = "false";

    @JsonProperty("location")
    String location = "false";

    @JsonProperty("aggregate_frequency")
    String aggregateFrequency = "0";

    @JsonProperty("contact_details")
    String contactDetails = "false";

    @JsonProperty("customer_id")
    String customerId = "false";

    @JsonProperty("offer_type")
    String offerType = "";

    public String getId() {
        return id;
    }

    public String getAllProps() {
        return allProps;
    }

    public void setAllProps(String allProps) {
        this.allProps = allProps;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAggregateFrequency() {
        return aggregateFrequency;
    }

    public void setAggregateFrequency(String aggregateFrequency) {
        this.aggregateFrequency = aggregateFrequency;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
}
