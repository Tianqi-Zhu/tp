package manageme.logic.commands.link;

import static java.util.Objects.requireNonNull;
import static manageme.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static manageme.logic.parser.CliSyntax.PREFIX_MODULE;
import static manageme.logic.parser.CliSyntax.PREFIX_NAME;
import static manageme.logic.parser.CliSyntax.PREFIX_TASK;

import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.model.Model;
import manageme.model.link.Link;

/**
 * Adds a link to the address book.
 */
public class AddLinkCommand extends Command {

    public static final String COMMAND_WORD = "addLink";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a link to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ADDRESS + "LINK ADDRESS "
            + "[Optional]" + PREFIX_MODULE + "ASSOCIATED_MODULE_NAME\n"
            + "[Optional]" + PREFIX_TASK + "ASSOCIATED_TASK_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Luminus "
            + PREFIX_ADDRESS + "https://luminus.nus.edu.sg/dashboard "
            + PREFIX_MODULE + "CS2103T"
            + PREFIX_TASK + "code";

    public static final String MESSAGE_SUCCESS = "New link added: %1$s";
    public static final String MESSAGE_DUPLICATE_LINK = "This link already exists in the address book";

    private final Link toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Link}
     */
    public AddLinkCommand(Link link) {
        requireNonNull(link);
        toAdd = link;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasLink(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_LINK);
        }

        model.addLink(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddLinkCommand // instanceof handles nulls
                && toAdd.equals(((AddLinkCommand) other).toAdd));
    }
}
