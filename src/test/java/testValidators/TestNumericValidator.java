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
import validators.NumericValidator;

/**
 * This class tests validatesNumeric functionalities according to the schema.
 * Number - any number.
 * Integer - only allow integers.
 */
public class TestNumericValidator {

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
        String testPayload = "34";
        JsonPrimitive expected = new JsonPrimitive(Integer.parseInt(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an valid integer.
     */
    @Test
    public void testIntegerValidation() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"integer\"}";
        String testPayload = "123";
        JsonPrimitive expected = new JsonPrimitive(Integer.parseInt(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an invalid integer. (converted to int by validator)
     */
    @Test
    public void testConvertFloatToInt() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"integer\"}";
        String testPayload = "3.14";
        JsonPrimitive expected = new JsonPrimitive(3);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an valid number.
     */
    @Test
    public void testNumberValidation() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\"}";
        String testPayload = "1.2358";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks an invalid number.
     */
    @Test
    public void testInvalidNumberValidation() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        String schema = "{ \"type\": \"number\"}";
        String testPayload = "Linking Park";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test checks the exception for non-numeric inputs.
     */
    @Test
    public void testNotNumericInput() throws ValidatorException, ParserException {
        thrown.expect(ParserException.class);
        //thrown.expectMessage("Name is empty!"); - not using since not finalized
        String schema = "{ \"type\": \"integer\"}";
        String testPayload = "true";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid multipleOf constraint.
     */
    @Test
    public void testValidMultipleOfConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"multipleOf\" : 2.5}";
        String testPayload = "7.5";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for valid multipleOf constraint.
     */
    @Test
    public void testInValidMultipleOfConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"multipleOf\" : 2.5}";
        String testPayload = "7.4";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid exclusiveMinimum constraint.
     */
    @Test
    public void testValidExclusiveMinimumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"exclusiveMinimum\" : 1.618}";
        String testPayload = "1.619";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid exclusiveMinimum constraint.
     */
    @Test
    public void testInvalidExclusiveMinimumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"exclusiveMinimum\" : 1.618}";
        String testPayload = "1.618";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid exclusiveMaximum constraint.
     */
    @Test
    public void testValidExclusiveMaximumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"exclusiveMaximum\" : 1.618}";
        String testPayload = "1.617";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid exclusiveMaximum constraint.
     */
    @Test
    public void testInvalidExclusiveMaximumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"exclusiveMaximum\" : 1.618}";
        String testPayload = "1.618";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid minimum constraint.
     */
    @Test
    public void testValidMinimumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"minimum\" : -5}";
        String testPayload = "-4.9";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid minimum constraint.
     */
    @Test
    public void testInvalidMinimumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"minimum\" : -5}";
        String testPayload = "-12.34";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid maximum constraint.
     */
    @Test
    public void testValidMaximumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"maximum\" : 1.618}";
        String testPayload = "1.617";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid maximum constraint.
     */
    @Test
    public void testInvalidMaximumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"maximum\" : -1.618}";
        String testPayload = "-1";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid enum constraint.
     */
    @Test
    public void testValidEnumConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"integer\", \"enum\" : [10,11,12]}";
        String testPayload = "10";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid enum constraint.
     */
    @Test
    public void testInvalidEnumConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"integer\", \"enum\" : [10,11,12]}";
        String testPayload = "13";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test checks for valid enum but invalid data.
     */
    @Test
    public void testValidEnumInvalidDataType() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"integer\", \"enum\" : [10.5,11.5,12.5]}";
        String testPayload = "10.5";
        // since 10.5 converted to 11
        JsonPrimitive expected = new JsonPrimitive(11);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test check for valid const constraint.
     */
    @Test
    public void testValidConstConstraint() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"number\", \"const\" : 1.234 }";
        String testPayload = "1.234";
        JsonPrimitive expected = new JsonPrimitive(Double.parseDouble(testPayload));
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }

    /**
     * This test checks for invalid const constraint.
     */
    @Test
    public void testInvalidConstConstraint() throws ValidatorException, ParserException {
        thrown.expect(ValidatorException.class);
        String schema = "{ \"type\": \"number\", \"const\" : 1.234 }";
        String testPayload = "1.23";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        NumericValidator.validateNumeric(schemaObject, testPayload);
    }

    /**
     * This test check for valid const but invalid data type.
     */
    @Test
    public void testValidConstInvalidDataType() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"integer\", \"const\" : 1.234 }";
        String testPayload = "1.234";
        JsonPrimitive expected = new JsonPrimitive(1);
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        JsonPrimitive result = NumericValidator.validateNumeric(schemaObject, testPayload);
        Assert.assertNotNull("Validator didn't respond with a JSON primitive", result);
        Assert.assertEquals("Didn't receive the expected primitive", expected, result);
    }
}
