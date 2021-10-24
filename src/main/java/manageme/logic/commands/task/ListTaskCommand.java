package manageme.logic.commands.task;

import static java.util.Objects.requireNonNull;
import static manageme.model.Model.PREDICATE_SHOW_ALL_TASKS;

import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.model.Model;

/**
 * Lists all tasks in the address book to the user.
 */
public class ListTaskCommand extends Command {

    public static final String COMMAND_WORD = "listTask";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
