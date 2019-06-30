package yappl.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    @JsonIgnore
    private int id = 0;
    private Purpose purpose = new Purpose();
    private Utilizer utilizer = new Utilizer();
    private List<Transformation> transformation = new ArrayList<>();
    @JsonProperty("valid_from")
    private String validFrom = "0000-00-00T00:00:00.00Z";
    @JsonProperty("exp_date")
    private String expDate = "0000-00-00T00:00:00.00Z";

    /**
     * @return The purposes that are available in the rule.
     */
    public Purpose getPurpose() {
        return purpose;
    }

    /**
     * @param purpose The purpose that should be set.
     */
    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    /**
     * @return The utilizers in the rule.
     */
    public Utilizer getUtilizer() {
        return utilizer;
    }

    /**
     * @param utilizer The Utilizer that should be set.
     */
    public void setUtilizer(Utilizer utilizer) {
        this.utilizer = utilizer;
    }

    /**
     * @return A list of transformations in the rule.
     */
    public List<Transformation> getTransformation() {
        return transformation;
    }

    /**
     * @param transformation The list of transformations that should be set.
     */
    public void setTransformation(List<Transformation> transformation) {
        this.transformation = transformation;
    }

    /**
     * @return The start of the validity of the rule in a String timestamp.
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * @param validFrom The valid from time as a String timestamp.
     */
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * @return The expiry date as a string timestamp.
     */
    public String getExpDate() {
        return expDate;
    }

    /**
     * @param expDate The expiry date that should be set as a string timestamp.
     */
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    /**
     * @return The id of the rule.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id that should be set for the Rule.
     */
    public void setId(int id) {
        this.id = id;
    }
}
