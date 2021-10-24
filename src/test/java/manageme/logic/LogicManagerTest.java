package manageme.logic;

import static manageme.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static manageme.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import manageme.logic.commands.AddCommand;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.CommandTestUtil;
import manageme.logic.commands.ListCommand;
import manageme.logic.commands.exceptions.CommandException;
import manageme.logic.parser.exceptions.ParseException;
import manageme.model.Model;
import manageme.model.ModelManager;
import manageme.model.ReadOnlyManageMe;
import manageme.model.UserPrefs;
import manageme.model.person.Person;
import manageme.storage.JsonManageMeStorage;
import manageme.storage.JsonUserPrefsStorage;
import manageme.storage.StorageManager;
import manageme.testutil.Assert;
import manageme.testutil.PersonBuilder;
import manageme.testutil.TypicalPersons;
import manageme.time.TimeManager;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonManageMeStorage addressBookStorage =
                new JsonManageMeStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        TimeManager time = new TimeManager(model.getManageMe());
        logic = new LogicManager(model, storage, time);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonAddressBookIoExceptionThrowingStub
        JsonManageMeStorage addressBookStorage =
                new JsonManageMeIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        TimeManager time = new TimeManager(model.getManageMe());
        logic = new LogicManager(model, storage, time);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY
                + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(TypicalPersons.AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredModuleList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredModuleList().remove(0));
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredTaskList().remove(0));
    }

    @Test
    public void getReadModuleList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getReadModuleList().remove(0));
    }

    @Test
    public void getUnfilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getUnfilteredTaskList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getManageMe(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        Assert.assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonManageMeIoExceptionThrowingStub extends JsonManageMeStorage {
        private JsonManageMeIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveManageMe(ReadOnlyManageMe addressBook, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
