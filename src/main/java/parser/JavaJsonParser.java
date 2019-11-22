package parser;

import com.google.gson.*;
import contants.ValidatorConstants;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import utils.GSONDataTypeConverter;
import validators.*;

/**
 * This class will parse a given JSON input according to a given schema.
 * Supported inout formats - String and Gson JsonObject
 */
public class JavaJsonParser {

    // Use without instantiating
    private JavaJsonParser() {
    }

    // Logger instance
    private static Log logger = LogFactory.getLog(JavaJsonParser.class.getName());

    // JSON parser instance
    private static JsonParser parser = new JsonParser();

    /**
     * This method parse a given JSON string according to the given schema. Both as string.
     *
     * @param inputString input String.
     * @param inputSchema input Schema.
     * @return corrected String.
     * @throws ValidatorException Exception occurs in validation process.
     * @throws ParserException    Exception occurs in data type parsing.
     */
    public static String parseJson(String inputString, String inputSchema) throws ValidatorException, ParserException {
        if (inputString != null && !inputString.isEmpty() && inputSchema != null && !inputSchema.isEmpty()) {
            JsonObject schemaObject;
            JsonElement schema = null;
            try {
                schema = parser.parse(inputSchema);
            } catch (JsonSyntaxException ex) {
                ValidatorException exception = new ValidatorException("Invalid JSON schema " + ex.getMessage());
                logger.error("Invalid JSON schema : " + ex.getMessage(), exception);
                throw exception;
            }
            if (schema.isJsonObject()) {
                // Handling empty JSON objects - valid for all inputs
                if(schema.toString().replaceAll("\\s+","").equals("{}")) {
                    return inputString;
                }
                schemaObject = schema.getAsJsonObject();
            } else if (schema.isJsonPrimitive()) {
                // if schema is primitive it should be a boolean
                boolean valid = schema.getAsBoolean();
                if (valid) {
                    return inputString;
                } else {
                    ValidatorException exception = new ValidatorException("JSON schema is not valid for all elements");
                    logger.error("JSON schema is false, so all validations will fail", exception);
                    throw exception;
                }
            } else {
                ValidatorException exception = new ValidatorException("Unexpected JSON schema");
                logger.error("JSON schema should be an object or boolean", exception);
                throw exception;
            }
            return parseJson(inputString, schemaObject);
        } else {
            ParserException exception = new ParserException("Invalid inputs");
            logger.error("Input json and schema should not be null", exception);
            throw exception;
        }
    }

    /**
     * This method will parse a given JSON string according to the given schema. Schema as an Object.
     * Can use this method when using caching.
     *
     * @param inputString input JSON string.
     * @param schema      already parsed JSON schema.
     * @return corrected JSON string.
     * @throws ValidatorException Exception occurs in validation process.
     * @throws ParserException    Exception occurs in data type parsing.
     */
    public static String parseJson(String inputString, Object schema) throws ValidatorException, ParserException {
        if (inputString != null && !inputString.isEmpty() && schema instanceof JsonObject) {
            JsonElement result = null;
            JsonObject schemaObject = (JsonObject) schema;
            if (((JsonObject) schema).has(ValidatorConstants.TYPE_KEY)) {
                String type = schemaObject.get(ValidatorConstants.TYPE_KEY).toString().replaceAll(
                        ValidatorConstants.REGEX, "");
                if (ValidatorConstants.BOOLEAN_KEYS.contains(type)) {
                    result = BooleanValidator.validateBoolean(schemaObject, inputString);
                } else if (ValidatorConstants.NOMINAL_KEYS.contains(type)) {
                    result = StringValidator.validateNominal(schemaObject, inputString);
                } else if (ValidatorConstants.NUMERIC_KEYS.contains(type)) {
                    result = NumericValidator.validateNumeric(schemaObject, inputString);
                } else if (ValidatorConstants.ARRAY_KEYS.contains(type)) {
                    result = ArrayValidator.validateArray(GSONDataTypeConverter.getMapFromString(inputString),
                            schemaObject);
                } else if (ValidatorConstants.NULL_KEYS.contains(type)) {
                    NullValidator.validateNull(schemaObject, inputString);
                    result = JsonNull.INSTANCE;
                } else if (ValidatorConstants.OBJECT_KEYS.contains(type)) {
                    JsonElement input = parser.parse(inputString);
                    if (input.isJsonObject()) {
                        result = ObjectValidator.validateObject(input.getAsJsonObject(), schemaObject);
                    } else {
                        ValidatorException exception = new ValidatorException("Expected a json object input");
                        logger.error("Expected a JSON as input but found : " + inputString, exception);
                        throw exception;
                    }
                }
                if (result != null) {
                    return result.toString();
                }
                return null;
            } else {
                ValidatorException exception = new ValidatorException("JSON schema should contain a type declaration");
                logger.error("JSON schema does not contains a type : " + schema, exception);
                throw exception;
            }
        }
        else {
            ParserException exception = new ParserException("Invalid inputs");
            logger.error("Input json and schema should not be null, schema should be a JSON object", exception);
            throw exception;
        }
    }
}
