package manageme.model.module;

import static java.util.Objects.requireNonNull;

import manageme.commons.util.AppUtil;

/**
 * Represents a module's link in the app.
 * Guarantees: immutable; is valid as declared in {@link #isValidModuleName(String)}
 */
public class ModuleName {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the module name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs a {@code ModuleName}.
     *
     * @param moduleName A valid mod name.
     */
    public ModuleName(String moduleName) {
        requireNonNull(moduleName);
        AppUtil.checkArgument(isValidModuleName(moduleName), MESSAGE_CONSTRAINTS);
        this.value = moduleName;
    }

    /**
     * Returns true if a given string is a valid module name.
     */
    public static boolean isValidModuleName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ModuleName // instanceof handles nulls
                && value.equals(((ModuleName) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
