package manageme.model.task;

import static manageme.testutil.Assert.assertThrows;
import static manageme.testutil.TypicalTasks.TASK_A;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TaskName(null));
    }

    @Test
    public void constructor_invalidTaskName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new TaskName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> TaskName.isValidName(null));

        // invalid name
        assertFalse(TaskName.isValidName("")); // empty string
        assertFalse(TaskName.isValidName(" ")); // spaces only

        // valid name
        assertTrue(TaskName.isValidName("do work")); // alphabets only
        assertTrue(TaskName.isValidName("12345")); // numbers only
        assertTrue(TaskName.isValidName("sleep by 12pm")); // alphanumeric characters
        assertTrue(TaskName.isValidName("Work on CS2103T")); // with capital letters
        assertTrue(TaskName.isValidName("Plan out next week timetable by this Friday")); // long names
    }

    @Test
    public void equals() {
        // same name, returns true
        assertTrue(TASK_A.getName().equals(TASK_A.getName()));

    }
}
