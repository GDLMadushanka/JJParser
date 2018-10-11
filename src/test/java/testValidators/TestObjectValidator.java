package testValidators;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import validators.ObjectValidator;

/**
 * This class will test the functionality of ObjectValidator class.
 */
public class TestObjectValidator {

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
        String testPayload = "{\"name\":\"Lahiru\"}";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonObject expected = (JsonObject) parser.parse(testPayload);
        ObjectValidator.validateObject(expected, schemaObject);
    }

    static String requiredValidationSchema = "{\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"name\":      { \"type\": \"string\" },\n" +
            "    \"email\":     { \"type\": \"string\" },\n" +
            "    \"age\":       { \"type\": \"integer\" }\n" +
            "  },\n" +
            "  \"required\": [\"name\", \"email\"]\n" +
            "}";

    /**
     * This test checks for invalid required constraint.
     */
    @Test
    public void testInvalidRequiredConstraint() throws ValidatorException, ParserException {
        String testPayload = "{\"name\": \"William Shakespeare\", \"email\": \"bill@stratford-upon-avon.co.uk\", \"age\":\"45\"}";
        String expectedPayload = "{\"name\": \"William Shakespeare\", \"email\": \"bill@stratford-upon-avon.co.uk\", \"age\":45}";
        JsonObject schemaObject = (JsonObject) parser.parse(requiredValidationSchema);
        JsonObject expected = (JsonObject) parser.parse(expectedPayload);
        JsonObject testObject = (JsonObject) parser.parse(testPayload);
        JsonObject result = ObjectValidator.validateObject(testObject, schemaObject);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected,result);
    }

    /**
     * This test checks for invalid properties constraint.
     */
    @Test
    public void testInvalidPropertiesConstraint() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        String testPayload = "{\"name\": \"William Shakespeare\", \"email\": \"bill@stratford-upon-avon.co.uk\", \"age\":\"invalid\"}";
        JsonObject schemaObject = (JsonObject) parser.parse(requiredValidationSchema);
        JsonObject testObject = (JsonObject) parser.parse(testPayload);
        ObjectValidator.validateObject(testObject, schemaObject);
    }

    /**
     * This test checks for valid required constraint.
     */
    @Test
    public void testValidRequiredConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String testPayload = "{\"name\": \"William Shakespeare\",\"age\":\"45\"}";
        JsonObject schemaObject = (JsonObject) parser.parse(requiredValidationSchema);
        JsonObject testObject = (JsonObject) parser.parse(testPayload);
        ObjectValidator.validateObject(testObject, schemaObject);
    }

    static String propertyCountSchema = "{\"type\": \"object\",\"minProperties\": 2,\"maxProperties\":4 }";

    /**
     * This test checks for valid minProperties and maxProperties constraint.
     */
    @Test
    public void testValidMinAndMaxProperties() throws ValidatorException, ParserException {
        String testPayload = "{ \"a\": 0, \"b\": 1, \"c\": 2 }";
        JsonObject schemaObject = (JsonObject) parser.parse(propertyCountSchema);
        JsonObject expected = (JsonObject) parser.parse(testPayload);
        JsonObject result = ObjectValidator.validateObject(expected, schemaObject);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected,result);
    }

    /**
     * This test checks for invalid minProperties constraint.
     */
    @Test
    public void testInvalidMinProperties() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String testPayload = "{ \"a\": 0 }";
        JsonObject schemaObject = (JsonObject) parser.parse(propertyCountSchema);
        JsonObject testObject = (JsonObject) parser.parse(testPayload);
        ObjectValidator.validateObject(testObject, schemaObject);
    }


    /**
     * This test checks for invalid maxProperties constraint.
     */
    @Test
    public void testInvalidMaxProperties() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String testPayload = "{ \"a\": 0, \"b\": 1, \"c\": 2 ,\"d\":3, \"e\":4}";
        JsonObject schemaObject = (JsonObject) parser.parse(propertyCountSchema);
        JsonObject testObject = (JsonObject) parser.parse(testPayload);
        ObjectValidator.validateObject(testObject, schemaObject);
    }
}
