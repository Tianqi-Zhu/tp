package manageme.commons.exceptions;

/**
 * Represents an error during comparing of start and end time in TimeManager
 */
public class TimeComparisonException extends Exception {
    public TimeComparisonException(String cause) {
        super(cause);
    }
}
