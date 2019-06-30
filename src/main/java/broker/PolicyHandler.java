package broker;

import broker.parsers.PurposeParser;
import broker.parsers.UtilizerParser;
import yappl.models.Policy;
import yappl.models.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolicyHandler {
    private List<Policy> savedPolicies = new ArrayList<>();
    private PurposeParser purposeParser = new PurposeParser();
    private UtilizerParser utilizerParser = new UtilizerParser();

    /**
     * Generates a Policy with a Rule for each of the 3 random available topics.
     * In each of the rules, a random utilizer is permitted access to messages on that topic.
     * On top of that, a rule that enables benchmarking is added to the Policy.
     * @return a Policy with randomly generated rules.+
     */
    Policy createFakePolicy() {
        Policy policy = new Policy();
        policy.setId(savedPolicies.size());
        List<String> topics = this.getRandomTopicIds();

        for (String topic : topics) {
            List<String> utilizers = this.getRandomUtilizerIds();

            policy.newRule(
                    Collections.singletonList(topic),
                    Collections.emptyList(),
                    Collections.singletonList(utilizers.get(0)),
                    Collections.emptyList(),
                    Collections.emptyList());
        }
        // Catch all rule for benchmarking
        policy.newRule(
                Collections.singletonList("benchmark"),
                Collections.emptyList(),
                Collections.singletonList("benchmarker"),
                Collections.emptyList(),
                Collections.emptyList());

        savedPolicies.add(policy);
        return policy;

    }

    List<String> getAllowedRepublicationTopics(Policy policy) {
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


    Policy findPolicyById(int id) {
        return this.savedPolicies.stream()
                .filter(policy -> policy.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of available purposes and returns 3 random ones from the list.
     * @return a list of 3 Policy ids.
     */
    private List<String> getRandomTopicIds() {
        List<String> availableIds = purposeParser.getAvailablePurposeTopicIds();
        Collections.shuffle(availableIds);
        return availableIds.subList(0, 3);
    }

    /**
     * Retrieves a list of available utilizers and returns 3 random ones from the list.
     * @return a list of 3 Utilizer ids.
     */
    private List<String> getRandomUtilizerIds() {
        List<String> availableIds = utilizerParser.getAvailableUtilizerIds();
        Collections.shuffle(availableIds);
        return availableIds.subList(0, 3);
    }
}
