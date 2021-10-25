package manageme.logic.commands.link;

import static java.util.Objects.requireNonNull;
import static manageme.model.Model.PREDICATE_SHOW_ALL_LINKS;

import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.model.Model;

public class ListLinkCommand extends Command {
    public static final String COMMAND_WORD = "listLink";

    public static final String MESSAGE_SUCCESS = "Listed all links";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredLinkList(PREDICATE_SHOW_ALL_LINKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

