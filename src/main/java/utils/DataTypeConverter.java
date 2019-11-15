package utils;

import contants.ValidatorConstants;
import exceptions.ParserException;

/**
 * Handle data type conversions for JSON parser.
 */
public class DataTypeConverter {

    // use without instantiating
    private DataTypeConverter() {
    }

    public static Boolean convertToBoolean(String value) throws ParserException {
        if (value != null && !value.isEmpty()) {
            value = value.replaceAll(ValidatorConstants.QUOTE_REPLACE_REGEX, "");
            if (value.equals("true") || value.equals("false")) {
                return Boolean.parseBoolean(value);
            }
            throw new ParserException("Cannot convert the sting : " + value + " to boolean");
        }
        throw new ParserException("Cannot convert an empty string to boolean");
    }

    public static int convertToInt(String value) throws ParserException {
        if (value != null && !value.isEmpty()) {
            value = value.replaceAll(ValidatorConstants.QUOTE_REPLACE_REGEX, "");
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException nfe) {
                throw new ParserException("NumberFormatException: " + nfe.getMessage());
            }
        }
        throw new ParserException("Empty value cannot convert to int");
    }

    public static double convertToDouble(String value) throws ParserException {
        if (value != null && !value.isEmpty()) {
            value = value.replaceAll(ValidatorConstants.QUOTE_REPLACE_REGEX, "");
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException nfe) {
                throw new ParserException("NumberFormatException: " + nfe.getMessage());
            }
        }
        throw new ParserException("Empty value cannot convert to double");
    }
}


