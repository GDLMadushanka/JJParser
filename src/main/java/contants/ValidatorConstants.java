package contants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Constants used in JSON parser.
 */
public class ValidatorConstants {

    private ValidatorConstants() {
    }

    public static final String REGEX = "^\"|\"$";
    public static String QUOTE_REPLACE_REGEX = "^\"|\"$";
    public static final String TYPE_KEY = "type";
    public static final String ITEM_KEY = "items";
    public static final String ENUM = "enum";
    public static final String CONST = "const";

    public static final Set<String> NUMERIC_KEYS = new HashSet<>(Arrays.asList("number", "integer"));
    public static final Set<String> BOOLEAN_KEYS = new HashSet<>(Arrays.asList("boolean"));
    public static final Set<String> NOMINAL_KEYS = new HashSet<>(Arrays.asList("String", "string"));
    public static final Set<String> OBJECT_KEYS = new HashSet<>(Arrays.asList("object"));
    public static final Set<String> ARRAY_KEYS = new HashSet<>(Arrays.asList("array"));
    public static final Set<String> NULL_KEYS = new HashSet<>(Arrays.asList("null"));
}
