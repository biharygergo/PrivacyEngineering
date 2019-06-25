package broker.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PurposeParser {
    List<Purpose> parsedPurposes = new ArrayList<>();

    public List<Purpose> getPurposesFromConfig() {
        try {
            // read the file
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/purpose_config.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            // map the purposes from jsonData to a list of broker.parsers.Purpose objects
            parsedPurposes = Arrays.asList(objectMapper.readValue(jsonData, Purpose[].class));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedPurposes;
    }

    public List<String> getAvailableGeneralTopicIds() {
        return Arrays.asList("temperature", "water", "light");
    }

    public List<String> getAvailablePurposeTopicIds() {
        List<Purpose> purposes = parsedPurposes;
        if (parsedPurposes.size() == 0) {
            purposes = getPurposesFromConfig();
        }
        return purposes.stream().map(Purpose::getId).collect(Collectors.toList());
    }
}
