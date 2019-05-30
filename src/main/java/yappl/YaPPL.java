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

public class YaPPL {
    ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    public void validate(String policy) throws IOException, YaPPLFormatException {
        JsonNode schemaNode = loadResource("/YaPPL_schema.json");
        JsonNode toValidate = mapper.readTree(policy);

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        try {
            final JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport report;
            report = schema.validate(toValidate);
            System.out.println(report);
            if (!report.isSuccess()) {
                throw new YaPPLFormatException();
            }

        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }

    public Policy parse(String jsonPolicy) throws IOException {
        Policy policy = new Policy();

        Policy parsedPolicy = mapper.readValue(jsonPolicy, Policy.class);
        int ruleId = 0;
        for (Preference preference : parsedPolicy.getPreference()) {
            preference.getRule().setId(ruleId++);
        }
        return policy;
    }

    public JsonNode loadResource(final String name)
            throws IOException {
        final String packageName = YaPPL.class.getPackage().getName();
        String packageBase = '/' + packageName.replace(".", "/");
        System.out.println(packageBase);
        return JsonLoader.fromResource(packageBase + name);
    }
}
