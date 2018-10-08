package testValidators;

import com.google.gson.*;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import validators.ArrayValidator;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        ArrayValidator.validateArray(getMapFromString(testPayload),schemaObject);
    }

    /**
     * This test checks array of valid items.
     */
    @Test
    public void testArrayOfValidItems() throws ValidatorException, ParserException {
        String schema = "{ \"type\": \"array\", \"items\":{\"type\":\"integer\"}}";
        String testPayload = "[8,13,21]";
        JsonObject schemaObject = (JsonObject) parser.parse(schema);
        ArrayValidator.validateArray(getMapFromString(testPayload), schemaObject);
    }




    // ValidateArray method need a Map, this method is used to create maps from strings.
    public Map.Entry<String, JsonElement> getMapFromString(String input) {
        JsonObject temp = new JsonObject();
        JsonArray arrayObject = (JsonArray) parser.parse(input);
        temp.add("test", arrayObject);
        Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        Iterator itr = entries.iterator();
        return (Map.Entry<String, JsonElement>)itr.next();
     }
}
