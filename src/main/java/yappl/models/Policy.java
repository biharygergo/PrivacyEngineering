package yappl.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Policy {
    @JsonProperty("_id")
    int id;
    List<Preference> preference;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Preference> getPreference() {
        return preference;
    }

    public void setPreference(List<Preference> preference) {
        this.preference = preference;
    }
}
