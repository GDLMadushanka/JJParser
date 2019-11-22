package integrationTests;

import com.google.gson.JsonParser;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import parser.JavaJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * This class will test integration scenarios among validators.
 */
public class TestJavaJsonParser {

    private static JsonParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init() {
        parser = new JsonParser();
    }

    /**
     * This test checks method that accept string as schema
     */
    @Test
    public void testStringMethod() throws ValidatorException, ParserException, IOException {
        //Reading input.json and validatingInput.json from files
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        //InputStream inputStream = classloader.getResourceAsStream("input.json");
        //String inputJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        InputStream inputStream = classloader.getResourceAsStream("schema.json");
        String inputJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        inputStream = classloader.getResourceAsStream("validatingInput.json");
        String validatingInput = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        //creating instances
        String result = JavaJsonParser.parseJson(validatingInput, inputJson);
        String expected = "{\"fruit\":\"12345\",\"price\":7.5,\"simpleObject\":{\"age\":234}," +
                "\"simpleArray\":[true,false,\"true\"],\"objWithArray\":{\"marks\":[34,45,56,67]}," +
                "\"arrayOfObjects\":[{\"maths\":90},{\"physics\":95},{\"chemistry\":65}],\"singleObjArray\":[1.618]," +
                "\"nestedObject\":{\"Lahiru\":{\"age\":27},\"Nimal\":{\"married\":true},\"Kamal\":{\"scores\":[24,45," +
                "67]}},\"nestedArray\":[[12,23,34],[true,false],[\"Linking Park\",\"Coldplay\"]]," +
                "\"allNumericArray\":[3,1,4],\"Hello\":890,\"nullArray\":[null,null,null],\"league_goals\":10}";
        Assert.assertEquals("Didn't receive the expected payload after parsing", expected, result);
    }

    /**
     * This test checks data type and structure correction.
     */
    @Test
    public void testTypeAndStructureCorrection() throws ValidatorException, ParserException {
        String schema = "{\n" +
                "\"schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                "\"type\": \"object\",\n" +
                "\"properties\": {\n" +
                "\"singleObjArray\": {\n" +
                "\"type\": \"array\",\n" +
                "\"items\": [{\n" +
                "\"type\": \"object\",\n" +
                "\"properties\": {\n" +
                "\"bla\": {\n" +
                "\"type\": \"integer\"\n" +
                "}}}]}}}";
        String inputJson = "{\"singleObjArray\":{\"bla\":\"3\"}}";
        String expected = "{\"singleObjArray\":[{\"bla\":3}]}";
        String result = JavaJsonParser.parseJson(inputJson, schema);
        Assert.assertEquals("Didn't receive the expected payload after parsing", expected, result);
        System.out.println(inputJson);
    }

    /**
     * This test checks the behaviour for invalid inputs.
     */
    @Test
    public void testInvalidInputs() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        JavaJsonParser.parseJson(null, null);
    }

    /**
     * This test checks the behaviour for {} schema inputs.
     */
    @Test
    public void testEmptySchemaInput() throws ValidatorException, ParserException {
        String result = JavaJsonParser.parseJson("123", "  {   }  ");
        Assert.assertEquals("Didn't receive the expected payload", "123", result);
    }

    /**
     * This test checks the behaviour for "true" schema inputs.
     */
    @Test
    public void testTrueSchemaInput() throws ValidatorException, ParserException {
        String result = JavaJsonParser.parseJson("123", "true");
        Assert.assertEquals("Didn't receive the expected payload", "123", result);
    }

    /**
     * This test checks the behaviour for "false" schema inputs.
     */
    @Test
    public void testFalseSchemaInput() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        thrown.expectMessage("JSON schema is not valid for all elements");
        JavaJsonParser.parseJson("123", "false");
    }
}
