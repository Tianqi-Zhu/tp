package manageme.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manageme.commons.core.Messages;
import manageme.logic.commands.AddCommand;
import manageme.logic.commands.ClearCommand;
import manageme.logic.commands.Command;
import manageme.logic.commands.DeleteCommand;
import manageme.logic.commands.EditCommand;
import manageme.logic.commands.ExitCommand;
import manageme.logic.commands.FindCommand;
import manageme.logic.commands.HelpCommand;
import manageme.logic.commands.ListCommand;
import manageme.logic.commands.calendar.NextMonthCommand;
import manageme.logic.commands.calendar.PreviousMonthCommand;
import manageme.logic.commands.calendar.ReadDayCommand;
import manageme.logic.commands.module.AddModuleCommand;
import manageme.logic.commands.module.DeleteModuleCommand;
import manageme.logic.commands.module.EditModuleCommand;
import manageme.logic.commands.module.FindModuleCommand;
import manageme.logic.commands.module.ListModuleCommand;
import manageme.logic.commands.module.ReadModuleCommand;
import manageme.logic.commands.task.AddTaskCommand;
import manageme.logic.commands.task.DeleteTaskCommand;
import manageme.logic.commands.task.EditTaskCommand;
import manageme.logic.commands.task.FindTaskCommand;
import manageme.logic.parser.calendar.ReadDayCommandParser;
import manageme.logic.parser.exceptions.ParseException;
import manageme.logic.parser.module.AddModuleCommandParser;
import manageme.logic.parser.module.DeleteModuleCommandParser;
import manageme.logic.parser.module.EditModuleCommandParser;
import manageme.logic.parser.module.FindModuleCommandParser;
import manageme.logic.parser.module.ReadModuleCommandParser;
import manageme.logic.parser.task.AddTaskCommandParser;
import manageme.logic.parser.task.DeleteTaskCommandParser;
import manageme.logic.parser.task.EditTaskCommandParser;
import manageme.logic.parser.task.FindTaskCommandParser;

/**
 * Parses user input.
 */
public class ManageMeParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddTaskCommand.COMMAND_WORD:
            return new AddTaskCommandParser().parse(arguments);

        case EditTaskCommand.COMMAND_WORD:
            return new EditTaskCommandParser().parse(arguments);

        case DeleteTaskCommand.COMMAND_WORD:
            return new DeleteTaskCommandParser().parse(arguments);

        case FindTaskCommand.COMMAND_WORD:
            return new FindTaskCommandParser().parse(arguments);

        case AddModuleCommand.COMMAND_WORD:
            return new AddModuleCommandParser().parse(arguments);

        case DeleteModuleCommand.COMMAND_WORD:
            return new DeleteModuleCommandParser().parse(arguments);

        case ReadModuleCommand.COMMAND_WORD:
            return new ReadModuleCommandParser().parse(arguments);

        case EditModuleCommand.COMMAND_WORD:
            return new EditModuleCommandParser().parse(arguments);

        case FindModuleCommand.COMMAND_WORD:
            return new FindModuleCommandParser().parse(arguments);

        case ListModuleCommand.COMMAND_WORD:
            return new ListModuleCommand();

        case NextMonthCommand.COMMAND_WORD:
            return new NextMonthCommand();

        case PreviousMonthCommand.COMMAND_WORD:
            return new PreviousMonthCommand();

        case ReadDayCommand.COMMAND_WORD:
            return new ReadDayCommandParser().parse(arguments);

        default:
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
