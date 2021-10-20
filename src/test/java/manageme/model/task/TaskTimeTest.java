package manageme.model.task;

import static manageme.testutil.Assert.assertThrows;
import static manageme.testutil.TypicalTasks.TASK_A;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskTimeTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TaskTime(null));
    }

    @Test
    public void constructor_invalidTaskTime_throwsIllegalArgumentException() {
        String invalidTime = "";
        assertThrows(IllegalArgumentException.class, () -> new TaskTime(invalidTime));
    }

    @Test
    public void isValidTime() {
        // null time
        assertThrows(NullPointerException.class, () -> TaskTime.isValidTaskTime(null));

        // invalid time
        assertFalse(TaskTime.isValidTaskTime("")); // empty string
        assertFalse(TaskTime.isValidTaskTime(" ")); // spaces only

        // valid time
        assertTrue(TaskTime.isValidTaskTime("abcd")); // alphabets only
        assertTrue(TaskTime.isValidTaskTime("12345")); // numbers only
        assertTrue(TaskTime.isValidTaskTime("abcd12345")); // alphanumeric characters
        assertTrue(TaskTime.isValidTaskTime("LM456NOP")); // with capital letters
        assertTrue(TaskTime.isValidTaskTime("11/05/2021T11:49:57")); // long time
    }

    @Test
    public void toDisplayString() {
        // empty TaskTime, returns ""
        assertTrue(TaskTime.empty().toDisplayString().equals(""));
    }
    @Test
    public void equals() {
        // same date/time, returns true
        assertTrue(TASK_A.getStart().equals(TASK_A.getStart()));
        assertTrue(TASK_A.getEnd().equals(TASK_A.getEnd()));
    }
    @Test
    public void empty() {
        // Empty TaskModule with value set as ""
        assertTrue(TaskTime.empty().value.equals(""));

        // Empty TaskModule with moduleName set as an empty Optional<String>
        assertTrue(TaskTime.empty().time.isEmpty());
    }
    @Test
    public void isEmpty() {
        // Empty TaskModule, returns true
        assertTrue(TaskTime.empty().isEmpty());

        // Empty TaskModule, returns false
        assertFalse(TASK_A.getStart().isEmpty());
    }

}
