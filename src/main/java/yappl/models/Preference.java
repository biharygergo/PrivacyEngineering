package yappl.models;

public class Preference {
    private Rule rule = new Rule();

    public Preference() {
    }

    public Preference(Rule rule) {
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
