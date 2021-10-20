package manageme.logic.commands.module;

import static java.util.Objects.requireNonNull;
import static manageme.model.Model.PREDICATE_SHOW_ALL_MODULES;

import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.model.Model;

/**
 * Lists all modules in the app to the user. Used to go back to
 * the full module list after a findMod command.
 */
public class ListModuleCommand extends Command {

    public static final String COMMAND_WORD = "listMod";

    public static final String MESSAGE_SUCCESS = "Listed all modules";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredModuleList(PREDICATE_SHOW_ALL_MODULES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
