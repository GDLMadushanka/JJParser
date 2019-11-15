package parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import contants.ValidatorConstants;
import exceptions.ParserException;
import exceptions.ValidatorException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import utils.GSONDataTypeConverter;
import validators.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;

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
        JsonObject schemaObject = null;
        JsonElement schema = parser.parse(inputSchema);
        if (schema.isJsonObject()) {
            schemaObject = schema.getAsJsonObject();
        } else if (schema.isJsonPrimitive()) {
            // if schema is primitive it should be a boolean
            boolean valid = schema.getAsBoolean();
            if (valid) {
                return inputString;
            } else  {
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
        JsonElement result = null;
        JsonObject schemaObject = (JsonObject) schema;
        String type = schemaObject.get(ValidatorConstants.TYPE_KEY).toString().replaceAll(ValidatorConstants
                .REGEX, "");
        if (inputString != null) {
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
        }
        return null;
    }
}
