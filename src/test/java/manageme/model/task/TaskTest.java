package manageme.model.task;

import static manageme.testutil.TypicalTasks.TASK_A;
import static manageme.testutil.TypicalTasks.TASK_B;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import manageme.logic.commands.task.TaskCommandTestUtil;
import manageme.testutil.TaskBuilder;

public class TaskTest {
    @Test
    public void isSameTask() {
        // same object -> returns true
        assertTrue(TASK_A.isSameTask(TASK_A));

        // null -> returns false
        assertFalse(TASK_A.isSameTask(null));

        // same name, all other attributes different -> returns true
        Task editedA = new TaskBuilder(TASK_A).withDescription(TaskCommandTestUtil.VALID_DESCRIPTION_B).withModule(
                        TaskCommandTestUtil.VALID_MODULE_B)
                .withStartDateTime(TaskCommandTestUtil.VALID_START_B).withEndDateTime(TaskCommandTestUtil.VALID_END_B)
                .build();
        assertTrue(TASK_A.isSameTask(editedA));

        // different name, all other attributes same -> returns false
        editedA = new TaskBuilder(TASK_A).withName(TaskCommandTestUtil.VALID_NAME_B).build();
        assertFalse(TASK_A.isSameTask(editedA));

        // name differs in case, all other attributes same -> returns false
        Task editedB = new TaskBuilder(TASK_B).withName(TaskCommandTestUtil.VALID_NAME_B.toLowerCase()).build();
        assertFalse(TASK_B.isSameTask(editedB));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = TaskCommandTestUtil.VALID_NAME_B + " ";
        editedB = new TaskBuilder(TASK_B).withName(nameWithTrailingSpaces).build();
        assertFalse(TASK_B.isSameTask(editedB));
    }
    @Test
    public void equals() {
        // same values -> returns true
        Task copyA = new TaskBuilder(TASK_A).build();
        assertTrue(TASK_A.equals(copyA));

        // same object -> returns true
        assertTrue(TASK_A.equals(TASK_A));

        // null -> returns false
        assertFalse(TASK_A.equals(null));

        // different type -> returns false
        assertFalse(TASK_A.equals(5));

        // different person -> returns false
        assertFalse(TASK_A.equals(TASK_B));

        // different name -> returns false
        Task editedA = new TaskBuilder(TASK_A).withName(TaskCommandTestUtil.VALID_NAME_B).build();
        assertFalse(TASK_A.equals(editedA));

        // different description -> returns false
        editedA = new TaskBuilder(TASK_A).withDescription(TaskCommandTestUtil.VALID_DESCRIPTION_B).build();
        assertFalse(TASK_A.equals(editedA));

        // different module -> returns false
        editedA = new TaskBuilder(TASK_A).withModule(TaskCommandTestUtil.VALID_MODULE_B).build();
        assertFalse(TASK_A.equals(editedA));

        // different start date/time -> returns false
        editedA = new TaskBuilder(TASK_A).withStartDateTime(TaskCommandTestUtil.VALID_START_B).build();
        assertFalse(TASK_A.equals(editedA));

        // different end date/time -> returns false
        editedA = new TaskBuilder(TASK_A).withEndDateTime(TaskCommandTestUtil.VALID_END_B).build();
        assertFalse(TASK_A.equals(editedA));
    }
}
