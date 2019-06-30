package yappl.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transformation {
    private String attribute;
    @JsonProperty("tr_func")
    private String truncateFunction;

    /**
     * @return The attribute of the transformation.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute The attribute that should be set for this transformation.
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return The truncate function of the transformation.
     */
    public String getTruncateFunction() {
        return truncateFunction;
    }

    /**
     * @param truncateFunction The function that should be set for the transformation.
     */
    public void setTruncateFunction(String truncateFunction) {
        this.truncateFunction = truncateFunction;
    }
}
