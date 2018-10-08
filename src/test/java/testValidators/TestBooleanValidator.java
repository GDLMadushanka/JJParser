package testValidators;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import validators.BooleanValidator;

/**
 * This class test the functionality of boolean validator class.
 */
public class TestBooleanValidator {

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
        String testPayload = "true";
        JsonPrimitive expected = new JsonPrimitive(true);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = BooleanValidator.validateBoolean(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for an invalid boolean string.
     */
    @Test
    public void testInvalidValue() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        //thrown.expectMessage("Name is empty!"); - not using since not finalized
        String schema = "{ \"type\": \"boolean\" }";
        String testPayload = "Banana";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        BooleanValidator.validateBoolean(schemaObject, testPayload);
    }

    /**
     * This test valid enum constraint.
     */
    @Test
    public void testValidEnumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"boolean\" ,\"enum\":[true,false]}";
        String testPayload = "false";
        JsonPrimitive expected = new JsonPrimitive(false);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = BooleanValidator.validateBoolean(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for not matching enum.
     */
    @Test
    public void testInvalidEnumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        //thrown.expectMessage("Name is empty!"); - not using since not finalized
        String schema = "{ \"type\": \"boolean\" ,\"enum\":[true,\"random text\"]}";
        String testPayload = "false";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        BooleanValidator.validateBoolean(schemaObject, testPayload);
    }

    /**
     * This test checks for matching enum but invalid data.
     */
    @Test
    public void testValidEnumButInvalidDataType() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        //thrown.expectMessage("Name is empty!"); - not using since not finalized
        String schema = "{ \"type\": \"boolean\" ,\"enum\":[\"In the End\",\"Numb\"]}";
        String testPayload = "In the End";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        BooleanValidator.validateBoolean(schemaObject, testPayload);
    }

    /**
     * This test checks for valid const constraint.
     */
    @Test
    public void testValidConstConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"boolean\" ,\"const\":true}";
        String testPayload = "true";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive expected = new JsonPrimitive(true);
        JsonPrimitive result = BooleanValidator.validateBoolean(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid const constraint.
     */
    @Test
    public void testInvalidConstConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"boolean\" ,\"const\":false}";
        String testPayload = "true";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        BooleanValidator.validateBoolean(schemaObject, testPayload);
    }

    /**
     * This test checks for valid const constraint but invalid data.
     */
    @Test
    public void testValidConstButInvalidData() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        String schema = "{ \"type\": \"boolean\" ,\"const\": 909 }";
        String testPayload = "909";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        BooleanValidator.validateBoolean(schemaObject, testPayload);
    }
}
