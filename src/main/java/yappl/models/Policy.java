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
    private int id;
    private List<Preference> preference = new ArrayList<>();

    /**
     * Creates a JSON string from the Policy object.
     *
     * @return The JSON string.
     * @throws JsonProcessingException Thrown if the object could not be serialized.
     */
    public String createPolicy() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    /**
     * Returns all purposes that are excluded in the policy.
     *
     * @return A list of purposes as strings.
     */
    public List<String> getExcludedPurposes() {
        List<String> excludedPurposes = new ArrayList<>();
        for (Preference preference : getPreference()) {
            excludedPurposes.addAll(preference.getRule().getPurpose().getExcluded());
        }
        return excludedPurposes;
    }

    /**
     * Returns all excluded utilizers from the policy.
     *
     * @return A list of utilizers as strings.
     */
    public List<String> getExcludedUtilizers() {
        List<String> excludedUtilizers = new ArrayList<>();
        for (Preference preference : getPreference()) {
            excludedUtilizers.addAll(preference.getRule().getUtilizer().getExcluded());
        }
        return excludedUtilizers;
    }

    /**
     * Adds a new rule to the policy.
     *
     * @param permittedPurpose  A list of permitted purposes.
     * @param excludedPurpose   A list of excluded purposes.
     * @param permittedUtilizer A list of permitted utilizers.
     * @param excludedUtilizer  A list of excluded utilizers.
     * @param transformation    A list of transformations in the rule.
     */
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


    /**
     * Returns all transformation rules available in the policy. Looks for transformations in active rules
     * only.
     *
     * @return A list of Rule objects with the permitted utilizers, purposes and transformations.
     */
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

    /**
     * Archives a rule based on its id.
     *
     * @param ruleId The rule to archive.
     */
    public void archiveRule(int ruleId) {
        Optional<Rule> rule = getRule(ruleId);
        if (rule.isPresent()) {
            rule.get().setExpDate(Instant.now().toString());
            rule.get().setId(-1);
        }
    }


    /**
     * Updates a rule and its properties.
     *
     * @param ruleId            The id of the rule to update.
     * @param permittedPurpose  The permitted purposes in the updated rule.
     * @param excludedPurpose   The excluded purposes in the updated rule.
     * @param permittedUtilizer The permitted utilizers in the updated rule.
     * @param excludedUtilizer  The excluded utilizers in the updated rule.
     * @param transformation    The transformations in the updated rule.
     */
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

    /**
     * Adds a rule to the policy if it's unique.
     *
     * @param rule The rule to add.
     */
    private void addRule(Rule rule) {
        if (isRuleUnique(rule)) {
            this.preference.add(new Preference(rule));
        }
    }

    /**
     * Checks whether a given rule already exists in the rules array.
     *
     * @param rule The rule to check.
     * @return Whether the Rule is unique or not.
     */
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

    /**
     * For a given purpose, it checks whether it is included in any of the exclusions.
     *
     * @param purpose The purpose to check.
     * @return Whether the purpose is permitted and not included in any of the exclusions.
     */
    public boolean isPurposeAllowed(String purpose) {
        return getPurpose().getPermitted().contains(purpose)
                && !getPurpose().getExcluded().contains(purpose);
    }

    /**
     * Returns all permitted and excluded purposes from the active rules.
     *
     * @return A Purpose object with all permitted and excluded purposes from the rules.
     */
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

    /**
     * Returns all permitted and excluded utilizers from all the active rules.
     *
     * @return A Utilizer object with all rules' permitted and excluded utilizers.
     */
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


    /**
     * Gets a rule by its id.
     *
     * @param ruleId The id of the rule to get.
     * @return An Optional rule that will contain null if the Rule is not found.
     */
    private Optional<Rule> getRule(int ruleId) {
        return getPreference().stream()
                .filter(preference -> preference.getRule().getId() == ruleId)
                .map(Preference::getRule).findFirst();
    }

    /**
     * Gets all rules that have not expired yet.
     *
     * @return A list of active rules.
     */
    private List<Rule> getAllActiveRules() {
        return this.getPreference().stream()
                .filter(preference -> preference.getRule().getExpDate().equals("0000-00-00T00:00:00.00Z"))
                .map(Preference::getRule)
                .collect(Collectors.toList());
    }

    /**
     * Gets all rules regardless of their expiration.
     *
     * @return A list of rules.
     */
    private List<Rule> getAllRules() {
        return this.getPreference().stream()
                .map(Preference::getRule)
                .collect(Collectors.toList());
    }


    /**
     * Gets the id of the Policy.
     *
     * @return The ID of the policy.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the policy.
     *
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the preferences of the policy.
     *
     * @return A list of preferences.
     */
    public List<Preference> getPreference() {
        return preference;
    }

    /**
     * Sets the list of preferences of the policy.
     *
     * @param preference The list of preferences to set.
     */
    public void setPreference(List<Preference> preference) {
        this.preference = preference;
    }
}
