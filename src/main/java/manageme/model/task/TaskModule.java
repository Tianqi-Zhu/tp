package manageme.model.task;

import static java.util.Objects.requireNonNull;
import static manageme.commons.util.AppUtil.checkArgument;

import java.util.Optional;


public class TaskModule {
    public static final String MESSAGE_CONSTRAINTS =
            "Modules should only contain alphanumeric characters " + "and spaces, and it should not be blank";;
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;
    public final Optional<String> moduleName;

    /**
     * Constructs a {@code TaskModule}.
     *
     * @param taskModule A Task Module.
     */
    public TaskModule(String taskModule) {
        requireNonNull(taskModule);
        checkArgument(isValidModule(taskModule), MESSAGE_CONSTRAINTS);
        this.value = taskModule;
        this.moduleName = Optional.of(taskModule);
    }

    /**
     * Constructs an empty {@code TaskModule}.
     */
    public TaskModule() {
        this.value = "";
        this.moduleName = Optional.empty();
    }

    /**
     * Returns true if a given string is a valid Module.
     */
    public static boolean isValidModule(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskModule // instanceof handles nulls
                && value.equals(((TaskModule) other).value)); // state check
    }

    public static TaskModule empty() {
        return new TaskModule();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
