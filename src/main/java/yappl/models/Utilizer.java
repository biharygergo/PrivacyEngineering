package yappl.models;

import java.util.ArrayList;
import java.util.List;

public class Utilizer {
    private List<String> permitted = new ArrayList<>();
    private List<String> excluded = new ArrayList<>();

    public List<String> getPermitted() {
        return permitted;
    }

    public void setPermitted(List<String> permitted) {
        this.permitted = permitted;
    }

    public List<String> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<String> excluded) {
        this.excluded = excluded;
    }
}
