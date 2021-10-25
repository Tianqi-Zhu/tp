package manageme.logic.commands.link;

import static java.util.Objects.requireNonNull;
import static manageme.logic.parser.CliSyntax.PREFIX_LINK;
import static manageme.logic.parser.CliSyntax.PREFIX_MODULE;

import java.util.List;

import manageme.commons.core.Messages;
import manageme.commons.core.index.Index;
import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.model.Model;
import manageme.model.link.Link;
import manageme.model.link.LinkModule;

public class DeleteModLinkCommand extends Command {
    public static final String COMMAND_WORD = "deleteModLink";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the link identified by the index number in a particular module panel.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_MODULE + "CS222"
            + PREFIX_LINK + " 1";

    public static final String MESSAGE_DELETE_LINK_SUCCESS = "Deleted Link: %1$s";
    public static final String MESSAGE_INVALID_INDEX = "Invalid index";

    private final LinkModule module;
    private final Index targetIndex;

    public DeleteModLinkCommand(LinkModule module, Index targetIndex) {
        this.targetIndex = targetIndex;
        this.module = module;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Link> lastShownList = model.getFilteredLinkList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LINK_DISPLAYED_INDEX);
        }

        try {
            Link linkDeleted = model.deleteModLink(module, targetIndex);
            return new CommandResult(String.format(MESSAGE_DELETE_LINK_SUCCESS, linkDeleted));
        } catch (RuntimeException e) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteModLinkCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteModLinkCommand) other).targetIndex)
                && module.equals(((DeleteModLinkCommand) other).module)); // state check
    }
}
