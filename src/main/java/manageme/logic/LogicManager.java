package manageme.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import manageme.commons.core.GuiSettings;
import manageme.commons.core.LogsCenter;
import manageme.logic.commands.ArchiveCommand;
import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.logic.parser.ManageMeParser;
import manageme.logic.parser.exceptions.ParseException;
import manageme.model.Model;
import manageme.model.ReadOnlyManageMe;
import manageme.model.module.Module;
import manageme.model.person.Person;
import manageme.model.task.Task;
import manageme.storage.Storage;
import manageme.time.Time;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final Time time;
    private final ManageMeParser manageMeParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage, Time time) {
        this.model = model;
        this.storage = storage;
        this.time = time;
        manageMeParser = new ManageMeParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = manageMeParser.parseCommand(commandText);
        if (command instanceof ArchiveCommand) {
            ArchiveCommand archiveCommand = (ArchiveCommand) command;
            archiveCommand.setStorage(storage);
        }
        commandResult = command.execute(model);

        try {
            time.updateTasks(model.getManageMe());
            storage.saveManageMe(model.getManageMe());

        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }
    @Override
    public ReadOnlyManageMe getManageMe() {
        return model.getManageMe();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return model.getFilteredModuleList();
    }

    @Override
    public ObservableList<Module> getReadModuleList() {
        return model.getReadModuleList();
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<Task> getUnfilteredTaskList() {
        return model.getUnfilteredTaskList();
    }

    @Override
    public Path getManageMeFilePath() {
        return model.getManageMeFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
