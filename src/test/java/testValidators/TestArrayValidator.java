package testValidators;

import com.google.gson.*;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import utils.GSONDataTypeConverter;
import validators.ArrayValidator;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class will test the functionality of ArrayValidator class.
 */
public class TestArrayValidator {

    private static JsonParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init() {
        parser = new JsonParser();
    }

    /**
     * This test checks null schema validation.
     */
    @Test
    public void testNullSchema() throws ValidatorException, ParserException {
        String schema = "{}";
        String testPayload = "[1,2,3]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }

    /**
     * This test checks array of items with valid data. (numeric,string,boolean)
     * todo add null also here
     */
    @Test
    public void testArrayOfItemsValid() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"," +
                "\"minLength\": 6},{ \"type\": \"boolean\" ,\"const\":true}]}";
        String testPayload = "[\"8\",\"Lahiru\",\"true\",\"Additional\"]";
        String expectedPayload = "[8,\"Lahiru\", true, \"Additional\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks array of items with invalid data (numeric,string,boolean)
     * todo add null also here
     */
    @Test
    public void testArrayOfItemsInvalid() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"," +
                "\"minLength\": 6},{ \"type\": \"boolean\" ,\"const\":true}]}";
        String testPayload = "[\"8\",\"Cold\",\"Play\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }

    /**
     * This test checks  object as items with valid data.
     */
    @Test
    public void testObjectAsItemsValid() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"integer\",\"minimum\": 46368}}";
        String testPayload = "[\"75025\",\"121393\",\"196418\"]";
        String expectedPayload = "[75025,121393,196418]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks  object as items with invalid data.
     */
    @Test
    public void testObjectAsItemsInvalid() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"integer\",\"exclusiveMinimum\": 46368}}";
        String testPayload = "[\"46368\",\"75025\",\"121393\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }

    /**
     * This test checks valid array inside array.
     * Single element array is corrected
     */
    @Test
    public void testArrayInsideArray() throws ValidatorException, ParserException {
        String schema = "{\"type\":\"array\", \"items\":[{\"type\": \"array\",\"items\":[{\"type\":\"integer\"}]}]}";
        String testPayload = "[[\"345\"]]";
        String expectedPayload = "[[345]]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an array for valid minItems constraint.
     */
    @Test
    public void testValidMinItemsConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"string\"},\"minItems\":2}";
        String testPayload = "[\"You\",\"and\",\"I\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(testPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an array for invalid minItems constraint.
     */
    @Test
    public void testInvalidMinItemsConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"string\"},\"minItems\":3}";
        String testPayload = "[\"Too\",\"Late\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
    }

    /**
     * This test checks an array for valid maxItems constraint.
     */
    @Test
    public void testValidMaxItemsConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"string\",\"enum\":[\"You\",\"I\",\"and\"]}," +
                "\"maxItems\":3}";
        String testPayload = "[\"You\",\"and\",\"I\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(testPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an array for invalid maxItems constraint.
     */
    @Test
    public void testInvalidMaxItemsConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"string\"},\"maxItems\":3}";
        String testPayload = "[\"What\",\"I\",\"Have\",\"Done\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
    }

    /**
     * This test checks an array for valid uniqueItems constraint.
     */
    @Test
    public void testValidUniqueItemsConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"boolean\"},\"uniqueItems\":true}";
        String testPayload = "[\"true\",\"false\"]";
        String expectedPayload = "[true,false]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an array for invalid uniqueItems constraint.
     */
    @Test
    public void testInvalidUniqueItemsConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":{ \"type\": \"boolean\"},\"uniqueItems\":true}";
        String testPayload = "[\"true\",\"false\",\"true\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
    }

    /**
     * This test checks additionalItems true constraint
     */
    @Test
    public void testAdditionalItemsTrue() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"},{\"type\": " +
                "\"boolean\"}],\"additionalItems\":true}";
        String testPayload = "[\"8\",\"Lahiru\",\"true\",\"true\"]";
        String expectedPayload = "[8,\"Lahiru\", true, \"true\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks additionalItems true constraint with empty objects
     */
    @Test
    public void testAdditionalItemsTrueEmptySchema() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":[{},{},{}]}";
        String testPayload = "[34,45,56]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(testPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }
    /**
     * This test checks additionalItems false constraint
     */
    @Test
    public void testAdditionalItemsFalse() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"},{\"type\": " +
                "\"boolean\"}],\"additionalItems\":false}";
        String testPayload = "[\"8\",\"Lahiru\",\"true\",\"true\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }

    /**
     * This test checks additionalItems as an empty object
     */
    @Test
    public void testAdditionalItemsAsEmptyObject() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"},{\"type\": " +
                "\"boolean\"}],\"additionalItems\":{}}";
        String testPayload = "[\"8\",\"Lahiru\",\"true\",\"true\"]";
        String expectedPayload = "[8,\"Lahiru\", true, \"true\"]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks non empty additionalItems
     */
    @Test
    public void testAdditionalItemsNonEmpty() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":[{\"type\":\"integer\"},{ \"type\": \"string\"},{\"type\": " +
                "\"boolean\"}],\"additionalItems\":{\"type\":\"integer\"}}";
        String testPayload = "[\"8\",\"Lahiru\",\"true\",\"12\",\"13\"]";
        String expectedPayload = "[8,\"Lahiru\", true, 12, 13]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    static String objectInsideArrayString =  "{\n" +
            "  \"type\": \"array\",\n" +
            "  \"items\": {\n" +
            "    \"type\": \"object\",\"properties\":{\n" +
            "      \"car\":{\"type\":\"string\"},\n" +
            "      \"prize\":{\"type\":\"integer\"}\n" +
            "    }\n" +
            "  }\n" +
            "}";

    /**
     * This test checks object inside array.
     */
    @Test
    public void testObjectInsideArray() throws ValidatorException, ParserException {
        String testPayload = "[{\"car\":\"lambo\",\"prize\":\"34\"}]";
        String expectedPayload = "[{\"car\":\"lambo\",\"prize\":34}]";
        JsonObject schemaObject = (JsonObject) parser.parse(objectInsideArrayString);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks invalid object inside array.
     */
    @Test
    public void testInvalidObjectInsideArray() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        String testPayload = "[{\"car\":\"lambo\",\"prize\":\"34.56\"}]";
        JsonObject schemaObject = (JsonObject) parser.parse(objectInsideArrayString);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }

    static String multipleObjectArray = "{\n" +
            "  \"type\": \"array\",\n" +
            "  \"items\": [{\"type\": \"object\",\"properties\":{\n" +
            "  \"car\":{\"type\":\"string\"},\n" +
            "  \"prize\":{\"type\":\"integer\"}}},{\"type\":\"object\"," +
            "\"properties\":{\"second\":{\"type\":\"number\"}}},{\"type\":\"boolean\"}]\n" +
            "}";

    /**
     * This test checks valid multi object array.
     */
    @Test
    public void testValidMultiObjectArray() throws ValidatorException, ParserException {
        String testPayload = "[{\"car\":\"lambo\",\"prize\":\"34\"},{\"second\":\"34.56\"},\"true\"]";
        String expectedPayload = "[{\"car\":\"lambo\",\"prize\":34},{\"second\":34.56},true]";
        JsonObject schemaObject = (JsonObject) parser.parse(multipleObjectArray);
        JsonArray expected = (JsonArray) parser.parse(expectedPayload);
        JsonArray result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload),
                schemaObject);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks invalid multi object array.
     */
    @Test
    public void testInvalidMultiObjectArray() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        String testPayload = "[{\"car\":\"lambo\",\"prize\":34.56},{\"second\":34.56},true]";
        JsonObject schemaObject = (JsonObject) parser.parse(objectInsideArrayString);
        ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(testPayload), schemaObject);
    }
}
