package testValidators;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ValidatorException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import validators.NullValidator;

/**
 * This class will test the functionality of the NullValidator class.
 */
public class TestNullValidator {

    private static JsonParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void init() {
        parser = new JsonParser();
    }

    /**
     * This test checks null string input validation.
     */
    @Test
    public void testNullStringInput() throws ValidatorException {
        String schema = "{\"type\":\"null\"}";
        String testPayload = "null";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NullValidator.validateNull(schemaObject, testPayload);
    }

    /**
     * This test checks "" input validation.
     */
    @Test
    public void testEmptyInput() throws ValidatorException {
        thrown.expect(ValidatorException.class);
        String schema = "{\"type\":\"null\"}";
        String testPayload = "";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NullValidator.validateNull(schemaObject, testPayload);
    }

    /**
     * This test checks null input validation.
     */
    @Test
    public void testNullInput() throws ValidatorException {
        String schema = "{\"type\":\"null\"}";
        String testPayload = null;
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NullValidator.validateNull(schemaObject, testPayload);
    }

    /**
     * This test checks an invalid null input.
     */
    @Test
    public void testInvalidNullInput() throws ValidatorException {
        thrown.expect(ValidatorException.class);
        String schema = "{\"type\":\"null\"}";
        String testPayload = "Banana";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NullValidator.validateNull(schemaObject, testPayload);
    }
}
