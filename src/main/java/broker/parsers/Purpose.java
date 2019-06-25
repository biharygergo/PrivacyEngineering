package broker.parsers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Purpose {
    @JsonProperty("id")
    private String id = "";

    @JsonProperty("added_properties")
    private List<String> addedProperties = new ArrayList<>();

    @JsonProperty("removed_properties")
    private List<String> removedProperties = new ArrayList<>();

    @JsonProperty("aggregate_frequency")
    private String aggregateFrequency = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAddedProperties() {
        return addedProperties;
    }

    public void setAddedProperties(List<String> addedProperties) {
        this.addedProperties = addedProperties;
    }

    public List<String> getRemovedProperties() {
        return removedProperties;
    }

    public void setRemovedProperties(List<String> removedProperties) {
        this.removedProperties = removedProperties;
    }

    public String getAggregateFrequency() {
        return aggregateFrequency;
    }

    public void setAggregateFrequency(String aggregateFrequency) {
        this.aggregateFrequency = aggregateFrequency;
    }
}
