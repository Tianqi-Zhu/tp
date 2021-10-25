package manageme.logic.parser.link;

import static java.util.Objects.requireNonNull;
import static manageme.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static manageme.logic.parser.CliSyntax.PREFIX_INDEX;
import static manageme.logic.parser.CliSyntax.PREFIX_MODULE;
import static manageme.logic.parser.CliSyntax.PREFIX_NAME;

import manageme.commons.core.Messages;
import manageme.commons.core.index.Index;
import manageme.logic.commands.link.DeleteModLinkCommand;
import manageme.logic.commands.link.EditLinkCommand;
import manageme.logic.parser.ArgumentMultimap;
import manageme.logic.parser.ArgumentTokenizer;
import manageme.logic.parser.Parser;
import manageme.logic.parser.ParserUtil;
import manageme.logic.parser.exceptions.ParseException;
import manageme.model.link.LinkModule;

public class DeleteModLinkCommandParser implements Parser<DeleteModLinkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteModLinkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MODULE, PREFIX_INDEX);

        Index index;
        LinkModule module;

        try {
            index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_NAME).get());
            module = ParserUtil.parseLinkModule(argMultimap.getValue(PREFIX_MODULE).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    EditLinkCommand.MESSAGE_USAGE), pe);
        }

        return new DeleteModLinkCommand(module, index);
    }
}
