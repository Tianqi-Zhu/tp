package manageme.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_LINK = new Prefix("l/");
    public static final Prefix PREFIX_INDEX = new Prefix("i/");
    //to add link to task
    public static final Prefix PREFIX_TASK = new Prefix("lt/");

    /* Task Prefix definitions */
    public static final Prefix PREFIX_MODULE = new Prefix("mod/");
    public static final Prefix PREFIX_START = new Prefix("s/");
    public static final Prefix PREFIX_END = new Prefix("e/");
}
