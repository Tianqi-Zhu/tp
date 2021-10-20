package manageme.logic.commands.task;

import static manageme.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static manageme.logic.parser.CliSyntax.PREFIX_END;
import static manageme.logic.parser.CliSyntax.PREFIX_MODULE;
import static manageme.logic.parser.CliSyntax.PREFIX_NAME;
import static manageme.logic.parser.CliSyntax.PREFIX_START;
import static manageme.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manageme.commons.core.index.Index;
import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.model.ManageMe;
import manageme.model.Model;
import manageme.model.person.Person;
import manageme.model.task.Task;
import manageme.model.task.TaskNameContainsKeywordsPredicate;
import manageme.testutil.EditTaskDescriptorBuilder;



public class TaskCommandTestUtil {
    public static final String VALID_NAME_A = "sleep early";
    public static final String VALID_NAME_B = "Do work";
    public static final String VALID_DESCRIPTION_A = "Early lesson tomorrow";
    public static final String VALID_DESCRIPTION_B = "Finish before Friday 2pm";
    public static final String VALID_MODULE_A = "CS1231";
    public static final String VALID_MODULE_B = "MA1521";
    public static final String VALID_START_A = "2021-10-07T22:00";
    public static final String VALID_START_B = "2021-10-09T14:00";
    public static final String VALID_END_A = "2021-10-07T23:59";
    public static final String VALID_END_B = "2021-10-09T20:00";

    public static final String NAME_DESC_A = " " + PREFIX_NAME + VALID_NAME_A;
    public static final String NAME_DESC_B = " " + PREFIX_NAME + VALID_NAME_B;
    public static final String DESCRIPTION_DESC_A = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_A;
    public static final String DESCRIPTION_DESC_B = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_B;
    public static final String MODULE_DESC_A = " " + PREFIX_MODULE + VALID_MODULE_A;
    public static final String MODULE_DESC_B = " " + PREFIX_MODULE + VALID_MODULE_B;
    public static final String START_DESC_A = " " + PREFIX_START + VALID_START_A;
    public static final String START_DESC_B = " " + PREFIX_START + VALID_START_B;
    public static final String END_DESC_A = " " + PREFIX_END + VALID_END_A;
    public static final String END_DESC_B = " " + PREFIX_END + VALID_END_B;


    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + " "; // whitespace not allowed in names
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION; //empty string not
    // allowed for description
    public static final String INVALID_MODULE_DESC = " " + PREFIX_MODULE; // empty string not allowed for
    // module
    public static final String INVALID_START_DESC = " " + PREFIX_START + "haha"; // unable to parse such a
    // string into a datetime
    public static final String INVALID_END_DESC = " " + PREFIX_END + "124012240"; // unable to parse such a
    // string into a datetime

    public static final EditTaskCommand.EditTaskDescriptor DESC_A;
    public static final EditTaskCommand.EditTaskDescriptor DESC_B;

    static {
        DESC_A = new EditTaskDescriptorBuilder().withName(VALID_NAME_A)
                .withDescription(VALID_DESCRIPTION_A).withModule(VALID_MODULE_A).withStartDateTime(VALID_START_A)
                .withEndDateTime(VALID_END_A).build();
        DESC_B = new EditTaskDescriptorBuilder().withName(VALID_NAME_B)
                .withDescription(VALID_DESCRIPTION_B).withModule(VALID_MODULE_B).withStartDateTime(VALID_START_B)
                .withEndDateTime(VALID_END_B).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the ManageMe book, filtered task list and selected task in {@code actualModel} remain
     * unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        ManageMe expectedManageMe = new ManageMe(actualModel.getManageMe());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedManageMe, actualModel.getManageMe());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());

        //final String[] splitName = task.getName().value.split("\\s+");
        final String[] splitName = task.getName().value.split("\\s+");
        model.updateFilteredTaskList(new TaskNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredTaskList().size());
    }
}

