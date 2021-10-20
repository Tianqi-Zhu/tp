package manageme.model.task;

import static java.util.Objects.requireNonNull;

import manageme.commons.util.AppUtil;

public class TaskName {

    public static final String MESSAGE_CONSTRAINTS = "Name can take any value, and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Reference on REGEX: https://www.regular-expressions.info/quickstart.html
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs a {@code TaskName}.
     *
     * @param taskName A valid Task name.
     */
    public TaskName(String taskName) {
        requireNonNull(taskName);
        AppUtil.checkArgument(isValidName(taskName), MESSAGE_CONSTRAINTS);
        this.value = taskName;
    }

    /**
     * Checks if name has a whitespace in front.
     * @param test the name to test/check
     * @return name check boolean
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskName // instanceof handles nulls
                && value.equals(((TaskName) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
