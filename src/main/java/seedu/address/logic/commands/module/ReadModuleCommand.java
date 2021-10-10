package seedu.address.logic.commands.module;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;

public class ReadModuleCommand extends Command {
    public static final String COMMAND_WORD = "readMod";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Display the module identified by the index number used in the displayed module list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Displayed module";

    private final Index targetIndex;

    public ReadModuleCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_DISPLAYED_INDEX);
        }

        Module moduleToDisplay = lastShownList.get(targetIndex.getZeroBased());
        model.updateReadModuleList(module -> module.equals(moduleToDisplay));
        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }
}
