package basic.jsonvalidate;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonTest {
    public static void main(String[] args) throws IOException, ProcessingException {

        // 准备写好的校验规则
        JsonNode fstabSchema = new ObjectMapper().readTree(
                "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"title\":\"Product\",\"description\":\"A product from Acme's catalog\",\"type\":\"object\",\"properties\":{\"id\":{\"description\":\"The unique identifier for a product\",\"type\":\"integer\"},\"name\":{\"description\":\"Name of the product\",\"type\":\"string\"},\"price\":{\"type\":\"number\",\"minimum\":0,\"exclusiveMinimum\":true}},\"required\":[\"id\",\"name\",\"price\"]}");
        // 准备好被检测的json
        JsonNode jsonToTest = new ObjectMapper().readTree("{\"name\":\"An ice sculpture\",\"price\":12.5}");
        // 拿到用来检测类对象
        JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(fstabSchema);

        ProcessingReport report = schema.validate(jsonToTest);

        System.out.println(report.isSuccess());
        System.out.println(report);

    }
}
