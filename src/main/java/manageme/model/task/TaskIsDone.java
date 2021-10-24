package manageme.model.task;

import static java.util.Objects.requireNonNull;


public class TaskIsDone {
    public static final String MESSAGE_CONSTRAINTS = "isDone should only be a yes or no,";

    public final boolean value;

    /**
     * Constructs a {@code TaskIsDone}, default to false.
     *
     */
    public TaskIsDone() {
        this.value = false;
    }

    /**
     * Constructs a {@code TaskIsDone} set to the given boolean.
     *
     * @param isDone A boolean to set isDone.
     */
    public TaskIsDone(boolean isDone) {
        requireNonNull(isDone);
        this.value = isDone;
    }

    @Override
    public String toString() {
        return value ? "yes" : "no";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskIsDone // instanceof handles nulls
                && value == (((TaskIsDone) other).value)); // state check
    }
}
