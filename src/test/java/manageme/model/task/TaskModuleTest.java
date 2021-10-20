package manageme.model.task;

import static manageme.testutil.Assert.assertThrows;
import static manageme.testutil.TypicalTasks.TASK_A;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskModuleTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TaskModule(null));
    }
    @Test
    public void constructor_invalidTaskModule_throwsIllegalArgumentException() {
        String invalidModule = "";
        assertThrows(IllegalArgumentException.class, () -> new TaskModule(invalidModule));
    }
    @Test
    public void isValidModule() {
        // null module
        assertThrows(NullPointerException.class, () -> TaskModule.isValidModule(null));

        // invalid module
        assertFalse(TaskModule.isValidModule("")); // empty string
        assertFalse(TaskModule.isValidModule(" ")); // spaces only
        assertFalse(TaskModule.isValidModule("^")); // only non-alphanumeric characters
        assertFalse(TaskModule.isValidModule("hello*")); // contains non-alphanumeric characters

        // valid module
        assertTrue(TaskModule.isValidModule("CSABCD")); // alphabets only
        assertTrue(TaskModule.isValidModule("12345")); // numbers only
        assertTrue(TaskModule.isValidModule("cs2100")); // alphanumeric characters
        assertTrue(TaskModule.isValidModule("CS2103T")); // with capital letters
    }
    @Test
    public void equals() {
        // same module, returns true
        assertTrue(TASK_A.getTaskModule().equals(TASK_A.getTaskModule()));
    }
    @Test
    public void empty() {
        // Empty TaskModule with value set as ""
        assertTrue(TaskModule.empty().value.equals(""));

        // Empty TaskModule with moduleName set as an empty Optional<String>
        assertTrue(TaskModule.empty().moduleName.isEmpty());
    }

}
