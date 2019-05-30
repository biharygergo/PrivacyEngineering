package yappl.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transformation {
    private String attribute;
    @JsonProperty("tr_func")
    private String truncateFunction;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTruncateFunction() {
        return truncateFunction;
    }

    public void setTruncateFunction(String truncateFunction) {
        this.truncateFunction = truncateFunction;
    }
}
