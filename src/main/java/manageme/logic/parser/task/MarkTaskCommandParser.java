package manageme.logic.parser.task;

import static manageme.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import manageme.commons.core.index.Index;
import manageme.logic.commands.task.MarkTaskCommand;
import manageme.logic.parser.Parser;
import manageme.logic.parser.ParserUtil;
import manageme.logic.parser.exceptions.ParseException;

public class MarkTaskCommandParser implements Parser<MarkTaskCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the MarkTaskCommand
     * and returns a MarkTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkTaskCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new MarkTaskCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkTaskCommand.MESSAGE_USAGE), pe);
        }
    }
}
