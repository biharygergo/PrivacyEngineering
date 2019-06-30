package yappl.models;

import java.util.ArrayList;
import java.util.List;

public class Utilizer {
    private List<String> permitted = new ArrayList<>();
    private List<String> excluded = new ArrayList<>();

    /**
     * @return The list of permitted utilizers as strings.
     */
    public List<String> getPermitted() {
        return permitted;
    }

    /**
     * @param permitted The list of permitted utilizers that should be set.
     */
    public void setPermitted(List<String> permitted) {
        this.permitted = permitted;
    }

    /**
     * @return The list of excluded utilizers as strings.
     */
    public List<String> getExcluded() {
        return excluded;
    }

    /**
     * @param excluded The list of excluded utilizers that should be set.
     */
    public void setExcluded(List<String> excluded) {
        this.excluded = excluded;
    }
}
