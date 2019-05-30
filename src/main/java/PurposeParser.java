import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PurposeParser {

    public List<Purpose> getPurposesFromConfig() {
        List<Purpose> parsedPurposes = new ArrayList<>();
        try {
            // read the file
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/purpose_config.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            // map the purposes from jsonData to a list of Purpose objects
            parsedPurposes = Arrays.asList(objectMapper.readValue(jsonData, Purpose[].class));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedPurposes;
    }
}
