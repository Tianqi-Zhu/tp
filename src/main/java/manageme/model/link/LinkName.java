package manageme.model.link;

import static java.util.Objects.requireNonNull;

import manageme.commons.util.AppUtil;

public class LinkName {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the link name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String name;

    /**
     * Constructs a {@code LinkName}.
     *
     * @param linkName A valid mod name.
     */
    public LinkName(String linkName) {
        requireNonNull(linkName);
        AppUtil.checkArgument(isValidLinkName(linkName), MESSAGE_CONSTRAINTS);
        this.name = linkName;
    }

    /**
     * Returns true if a given string is a valid link name.
     */
    public static boolean isValidLinkName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LinkName // instanceof handles nulls
                && name.equals(((LinkName) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
