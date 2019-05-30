import org.junit.Test;
import yappl.YaPPL;
import yappl.YaPPLFormatException;
import yappl.models.Policy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class YaPPLTests {

    @Test
    public void loadSchema() {
        YaPPL yaPPL = new YaPPL();
        try {
            yaPPL.loadResource("/YaPPL_schema.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void parseValidPolicy() throws IOException, URISyntaxException {
        YaPPL yaPPL = new YaPPL();


        String loadedJson = this.loadFileAsString("valid_policy.json");
        Policy parsed = yaPPL.parse(loadedJson);
        System.out.println(parsed);

    }

    @Test
    public void parseAndValidateValidPolicy() throws IOException, URISyntaxException, YaPPLFormatException {
        YaPPL yaPPL = new YaPPL();

        String loadedJson = this.loadFileAsString("valid_policy.json");
        yaPPL.validate(loadedJson);

    }

    private String loadFileAsString(String fileName) throws URISyntaxException, IOException {
        URL url = YaPPLTests.class.getResource("yappl/" + fileName);
        Path resPath = java.nio.file.Paths.get(url.toURI());
        String result = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        return result;

    }
}
