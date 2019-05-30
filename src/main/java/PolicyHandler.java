import yappl.models.Policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PolicyHandler {
    List<Policy> savedPolicies = new ArrayList<>();
    PurposeParser purposeParser = new PurposeParser();

    public Policy createFakePolicy() {
        Policy policy = new Policy();
        policy.setId(savedPolicies.size());
        List<String> topics = this.getRandomTopicIds();
        policy.newRule(Arrays.asList(topics.get(0),
                topics.get(1)), Arrays.asList(topics.get(2)),
                new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
        savedPolicies.add(policy);
        return policy;

    }

    public Policy findPolicyById(int id) {
        return this.savedPolicies.stream()
                .filter(policy -> policy.getId() == id)
                .findFirst()
                .get();
    }

    private List<String> getRandomTopicIds() {
        List<String> availableIds = purposeParser.getAvailableTopicIds();
        Collections.shuffle(availableIds);
        return availableIds.subList(0, 3);
    }
}
