package manageme.logic.parser.link;

import static java.util.Objects.requireNonNull;
import static manageme.logic.parser.CliSyntax.PREFIX_INDEX;
import static manageme.logic.parser.CliSyntax.PREFIX_MODULE;

import java.util.stream.Stream;

import manageme.commons.core.Messages;
import manageme.commons.core.index.Index;
import manageme.logic.commands.link.AddLinkCommand;
import manageme.logic.commands.link.DeleteModLinkCommand;
import manageme.logic.parser.ArgumentMultimap;
import manageme.logic.parser.ArgumentTokenizer;
import manageme.logic.parser.Parser;
import manageme.logic.parser.ParserUtil;
import manageme.logic.parser.Prefix;
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

        if (!arePrefixesPresent(argMultimap, PREFIX_MODULE, PREFIX_INDEX) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddLinkCommand.MESSAGE_USAGE));
        }

        Index index;
        LinkModule module;

        try {
            index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
            module = ParserUtil.parseLinkModule(argMultimap.getValue(PREFIX_MODULE).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteModLinkCommand.MESSAGE_USAGE), pe);
        }

        return new DeleteModLinkCommand(module, index);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
