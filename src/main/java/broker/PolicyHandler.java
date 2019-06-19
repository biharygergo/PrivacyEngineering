package broker;

import broker.parsers.PurposeParser;
import broker.parsers.UtilizerParser;
import yappl.models.Policy;
import yappl.models.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PolicyHandler {
    List<Policy> savedPolicies = new ArrayList<>();
    PurposeParser purposeParser = new PurposeParser();
    UtilizerParser utilizerParser = new UtilizerParser();

    public Policy createFakePolicy() {
        Policy policy = new Policy();
        policy.setId(savedPolicies.size());
        List<String> topics = this.getRandomTopicIds();

        for (String topic : topics) {
            List<String> utilizers = this.getRandomUtilizerIds();

            policy.newRule(
                    Arrays.asList(topic),
                    Arrays.asList(),
                    Arrays.asList(utilizers.get(0)),
                    Arrays.asList(),
                    Arrays.asList());
        }
        savedPolicies.add(policy);
        return policy;

    }

    public List<String> getAllowedRepublicationTopics(Policy policy) {
        List<String> topics = new ArrayList<>();
        for (Rule rule : policy.getTrRules()) {
            for (String permittedPurpose : rule.getPurpose().getPermitted()) {
                for (String permittedUtilizer : rule.getUtilizer().getPermitted()) {
                    topics.add(permittedUtilizer + "/" + permittedPurpose);
                }
            }
        }
        return topics;
    }


    public boolean isUtilizerAllowedForTopic(String topic, String utilizer, Policy policy) {
        List<Rule> rules = policy.getPreference().stream().map(preference -> preference.getRule()).collect(Collectors.toList());
        for (Rule rule : rules) {
            if (rule.getPurpose().getPermitted().contains(topic) && rule.getUtilizer().getPermitted().contains(utilizer)) {
                return true;
            }
        }
        return false;
    }


    public Policy findPolicyById(int id) {
        return this.savedPolicies.stream()
                .filter(policy -> policy.getId() == id)
                .findFirst()
                .get();
    }

    private List<String> getRandomTopicIds() {
        List<String> availableIds = purposeParser.getAvailablePurposeTopicIds();
        Collections.shuffle(availableIds);
        return availableIds.subList(0, 3);
    }

    private List<String> getRandomUtilizerIds() {
        List<String> availableIds = utilizerParser.getAvailableUtilizerIds();
        Collections.shuffle(availableIds);
        return availableIds.subList(0, 3);
    }
}
