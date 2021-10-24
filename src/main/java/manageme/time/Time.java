package manageme.time;

import manageme.model.ReadOnlyManageMe;

/**
 * API of the Time component
 */
public interface Time {

    /** Starts the Time component to constantly check tasks' start and end time. */
    void startTime();

    void updateTasks(ReadOnlyManageMe manageMe);
}
