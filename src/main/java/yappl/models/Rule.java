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

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Utilizer getUtilizer() {
        return utilizer;
    }

    public void setUtilizer(Utilizer utilizer) {
        this.utilizer = utilizer;
    }

    public List<Transformation> getTransformation() {
        return transformation;
    }

    public void setTransformation(List<Transformation> transformation) {
        this.transformation = transformation;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
