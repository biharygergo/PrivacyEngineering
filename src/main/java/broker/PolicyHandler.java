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
