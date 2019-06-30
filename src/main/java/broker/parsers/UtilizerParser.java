package broker.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilizerParser {

    /**
     * Parses utilizer_config.json and maps the data to a list of Utilizers.
     * @return List<Utilizer> contained in utilizer_config.json
     */
    public List<Utilizer> getUtilizersFromConfig() {
        List<Utilizer> parsedUtilizers = new ArrayList<>();

        try {
            // read the file
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/utilizer_config.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            // map the purposes from jsonData to a list of Purpose objects
            parsedUtilizers = Arrays.asList(objectMapper.readValue(jsonData, Utilizer[].class));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedUtilizers;
    }

    public List<String> getAvailableUtilizerIds() {
        List<Utilizer> utilizers = getUtilizersFromConfig();
        return utilizers.stream().map(utilizer -> utilizer.getId()).collect(Collectors.toList());
    }
}

