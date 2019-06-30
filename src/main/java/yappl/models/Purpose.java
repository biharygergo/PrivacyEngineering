package yappl.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Purpose {
    private List<String> permitted = new ArrayList<>();
    private List<String> excluded = new ArrayList<>();

    /**
     * Returns all permitted purposes.
     *
     * @return A list of permitted purposes.
     */
    public List<String> getPermitted() {
        return permitted;
    }

    /**
     * Sets the permitted purposes.
     *
     * @param permitted A list of permitted purposes.
     */
    public void setPermitted(List<String> permitted) {
        this.permitted = permitted;
    }

    /**
     * @return The purposes that are excluded.
     */
    public List<String> getExcluded() {
        return excluded;
    }

    /**
     * @param excluded The purposes that should be excluded.
     */
    public void setExcluded(List<String> excluded) {
        this.excluded = excluded;
    }
}
