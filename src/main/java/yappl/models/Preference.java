package yappl.models;

/**
 * Preference is a convenience class that encapsulates a Rule.
 */
public class Preference {
    private Rule rule = new Rule();

    /**
     * Creates a new Preference with a Rule.
     *
     * @param rule The rule to set.
     */
    Preference(Rule rule) {
        this.rule = rule;
    }

    /**
     * Gets the Rule object in the Preference.
     *
     * @return The rule object.
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the Rule object of the Preference.
     *
     * @param rule The rule to set.
     */
    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
