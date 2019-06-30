package yappl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import yappl.models.Policy;
import yappl.models.Preference;

import java.io.IOException;

/**
 * Helper class to parse and validate YaPPL policies.
 */
public class YaPPL {
    static ObjectMapper mapper = new ObjectMapper();

    /**
     * Validates a JSON policy.
     * @param policy The JSON string to validate
     * @throws IOException Thrown when the schema resource file is not found.
     * @throws YaPPLFormatException Thrown when there have been errors validating the schema.
     */
    public static void validate(String policy) throws IOException, YaPPLFormatException {
        JsonNode schemaNode = loadResource("/YaPPL_schema.json");
        JsonNode toValidate = mapper.readTree(policy);

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        try {
            final JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport report;
            report = schema.validate(toValidate);
            if (!report.isSuccess()) {
                throw new YaPPLFormatException();
            }

        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a JSON policy string into a Policy object.
     * @param jsonPolicy The JSON policy string.
     * @return The parsed Policy object
     * @throws IOException Thrown if the policy could not be read.
     */
    public static Policy parse(String jsonPolicy) throws IOException {

        Policy parsedPolicy = mapper.readValue(jsonPolicy, Policy.class);
        int ruleId = 0;
        for (Preference preference : parsedPolicy.getPreference()) {
            preference.getRule().setId(ruleId++);
        }
        return parsedPolicy;
    }

    /**
     * Loads a JSON resource by its name.
     * @param name The name of the resource file.
     * @return Returns a usable JsonNode object.
     * @throws IOException Thrown if the file could not be read.
     */
    public static JsonNode loadResource(final String name)
            throws IOException {
        final String packageName = YaPPL.class.getPackage().getName();
        String packageBase = '/' + packageName.replace(".", "/");
        return JsonLoader.fromResource(packageBase + name);
    }

}
