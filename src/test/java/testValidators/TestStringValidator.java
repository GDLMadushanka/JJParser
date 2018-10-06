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
import validators.StringValidator;

/**
 * This class will test the StringValidator functionalists according to the schema.
 */
public class TestStringValidator {

    private static JsonParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init() {
        parser = new JsonParser();
    }

    /**
     * This test checks a valid minLength condition.
     */
    @Test
    public void testMinLengthConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"string\",\"minLength\": 2}";
        String testPayload = "Banana";
        JsonPrimitive expected = new JsonPrimitive(testPayload);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = StringValidator.parseNominal(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an invalid minLength condition.
     */
    @Test
    public void testInvalidMinLengthConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        //thrown.expectMessage("Name is empty!"); - not using since not finalized

        String schema = "{ \"type\": \"string\",\"minLength\": 10}";
        String testPayload = "Banana";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        StringValidator.parseNominal(schemaObject, testPayload);
    }

}
