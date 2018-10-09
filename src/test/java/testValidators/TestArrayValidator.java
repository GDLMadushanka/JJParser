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
        String testPayload = "[\"8\",\"Lahiru\",\"true\"]";
        String expectedPayload = "[8,\"Lahiru\", true]";
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
}

//todo debug logs and more comments