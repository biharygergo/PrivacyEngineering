package yappl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Policy {
    @JsonProperty("_id")
    int id;
    List<Preference> preference = new ArrayList<>();

    public String createPolicy() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public List<String> getExcludedPurposes() {
        List<String> excludedPurposes = new ArrayList<>();
        for (Preference preference : getPreference()) {
            excludedPurposes.addAll(preference.getRule().getPurpose().getExcluded());
        }
        return excludedPurposes;
    }

    public List<String> getExcludedUtilizers() {
        List<String> excludedUtilizers = new ArrayList<>();
        for (Preference preference : getPreference()) {
            excludedUtilizers.addAll(preference.getRule().getUtilizer().getExcluded());
        }
        return excludedUtilizers;
    }

    public void newRule(List<String> permittedPurpose,
                        List<String> excludedPurpose,
                        List<String> permittedUtilizer,
                        List<String> excludedUtilizer,
                        List<Transformation> transformation) {
        Rule rule = new Rule();
        rule.getPurpose().setPermitted(permittedPurpose);
        rule.getPurpose().setExcluded(excludedPurpose);
        rule.getUtilizer().setPermitted(permittedUtilizer);
        rule.getUtilizer().setExcluded(excludedUtilizer);
        rule.setTransformation(transformation);
        rule.setId(getPreference().size());
        rule.setValidFrom(Instant.now().toString());

        addRule(rule);
    }


    public List<Rule> getTrRules() {
        List<Rule> transformationRules = new ArrayList<>();
        for (Rule rule : getAllActiveRules()) {
            Rule transformationRule = new Rule();
            transformationRule.getPurpose().setPermitted(rule.getPurpose().getPermitted());
            transformationRule.getUtilizer().setPermitted(rule.getUtilizer().getPermitted());
            transformationRule.setTransformation(rule.getTransformation());
            transformationRules.add(transformationRule);
        }
        return transformationRules;
    }

    public void archiveRule(int ruleId) {
        Optional<Rule> rule = getRule(ruleId);
        if (rule.isPresent()) {
            rule.get().setExpDate(Instant.now().toString());
            rule.get().setId(-1);
        }
    }


    public void updateRule(int ruleId, List<String> permittedPurpose, List<String> excludedPurpose, List<String> permittedUtilizer, List<String> excludedUtilizer, List<Transformation> transformation) {
        Optional<Rule> optionalRule = getRule(ruleId);
        if (!optionalRule.isPresent()) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Rule role = optionalRule.get();
            Rule ruleToAdd = mapper.readValue(mapper.writeValueAsString(role), Rule.class);
            ruleToAdd.getPurpose().getPermitted().addAll(permittedPurpose);
            ruleToAdd.getPurpose().getExcluded().addAll(excludedPurpose);
            ruleToAdd.getUtilizer().getPermitted().addAll(permittedUtilizer);
            ruleToAdd.getUtilizer().getExcluded().addAll(excludedUtilizer);
            ruleToAdd.getTransformation().addAll(transformation);
            newRule(ruleToAdd.getPurpose().getPermitted(), ruleToAdd.getPurpose().getExcluded(),
                    ruleToAdd.getUtilizer().getPermitted(), ruleToAdd.getUtilizer().getExcluded(), ruleToAdd.getTransformation());
            archiveRule(ruleId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRule(Rule rule) {
        if (isRuleUnique(rule)) {
            this.preference.add(new Preference(rule));
        }
    }

    private boolean isRuleUnique(Rule rule) {
        for (Rule savedRule : getAllRules()) {
            if (savedRule.getPurpose() == rule.getPurpose())
                if (savedRule.getUtilizer() == rule.getUtilizer())
                    if (savedRule.getTransformation() == rule.getTransformation())
                        if (savedRule.getExpDate().equals(rule.getExpDate()))
                            return false;
        }
        return true;
    }

    public Purpose getPurpose() {
        Purpose purpose = new Purpose();
        for (Rule rule : getAllActiveRules()) {
            purpose.getPermitted().addAll(rule.getPurpose().getPermitted());
            purpose.getExcluded().addAll(rule.getPurpose().getExcluded());
        }
        purpose.setPermitted(purpose.getPermitted().stream().distinct().collect(Collectors.toList()));
        purpose.setExcluded(purpose.getExcluded().stream().distinct().collect(Collectors.toList()));
        return purpose;

    }

    public Utilizer getUtilizer() {
        Utilizer utilizer = new Utilizer();
        for (Rule rule : getAllActiveRules()) {
            utilizer.getPermitted().addAll(rule.getUtilizer().getPermitted());
            utilizer.getExcluded().addAll(rule.getUtilizer().getExcluded());
        }
        utilizer.setPermitted(utilizer.getPermitted().stream().distinct().collect(Collectors.toList()));
        utilizer.setExcluded(utilizer.getExcluded().stream().distinct().collect(Collectors.toList()));
        return utilizer;

    }


    private Optional<Rule> getRule(int ruleId) {
        return getPreference().stream()
                .filter(preference -> preference.getRule().getId() == ruleId)
                .map(preference -> preference.getRule()).findFirst();
    }

    private List<Rule> getAllActiveRules() {
        return this.getPreference().stream()
                .filter(preference -> preference.getRule().getExpDate().equals("0000-00-00T00:00:00.00Z"))
                .map(preference -> preference.getRule())
                .collect(Collectors.toList());
    }

    private List<Rule> getAllRules() {
        return this.getPreference().stream()
                .map(preference -> preference.getRule())
                .collect(Collectors.toList());
    }


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
