package manageme.model.task;

import static manageme.testutil.Assert.assertThrows;
import static manageme.testutil.TypicalTasks.TASK_A;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class TaskDescriptionTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TaskDescription(null));
    }

    @Test
    public void constructor_invalidTaskDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new TaskDescription(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> TaskDescription.isValidDescription(null));

        // invalid description
        assertFalse(TaskDescription.isValidDescription("")); // empty string
        assertFalse(TaskDescription.isValidDescription(" ")); // spaces only

        // valid description
        assertTrue(TaskDescription.isValidDescription("do work")); // alphabets only
        assertTrue(TaskDescription.isValidDescription("12345")); // numbers only
        assertTrue(TaskDescription.isValidDescription("sleep by 12pm")); // alphanumeric characters
        assertTrue(TaskDescription.isValidDescription("Work on CS2103T")); // with capital letters
        assertTrue(TaskDescription.isValidDescription("Plan out next week timetable by this Friday"));
        // long description
    }

    @Test
    public void equals() {
        // same description, returns true
        assertTrue(TASK_A.getDescription().equals(TASK_A.getDescription()));

    }
}
