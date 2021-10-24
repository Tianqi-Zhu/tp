package manageme.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manageme.commons.core.GuiSettings;
import manageme.commons.core.LogsCenter;
import manageme.logic.Logic;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.logic.parser.exceptions.ParseException;
import manageme.ui.calendar.CalendarPanel;
import manageme.ui.module.ModuleListPanel;
import manageme.ui.module.ModuleWindow;
import manageme.ui.task.TaskListPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MmMainWindow extends UiPart<Stage> {

    private static final String FXML = "MmMainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ModuleListPanel moduleListPanel;
    private TaskListPanel taskListPanel;
    private CalendarPanel calendarPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private ModuleWindow moduleWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private HBox contentPlaceholder;

    @FXML
    private StackPane moduleListPanelPlaceholder;

    @FXML
    private StackPane taskListPanelPlaceholder;

    @FXML
    private StackPane calendarPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private Label module;

    @FXML
    private Label task;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MmMainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
        moduleWindow = new ModuleWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        moduleListPanel = new ModuleListPanel(logic.getFilteredModuleList());
        moduleListPanelPlaceholder.getChildren().add(moduleListPanel.getRoot());

        taskListPanel = new TaskListPanel(logic.getFilteredTaskList());
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());

        calendarPanel = new CalendarPanel(logic.getUnfilteredTaskList());
        calendarPanelPlaceholder.getChildren().add(calendarPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Opens the module window.
     */
    @FXML
    private void handleModule() {
        moduleWindow.display(logic.getReadModuleList());
    }

    /**
     * Handles the various command on calendar.
     * Passes control over to {@code CalendarListPanel} to execute the command.
     *
     * @param feedbackToSystem the respective commands to be executed
     */
    private void handleCalendar(String feedbackToSystem) {
        calendarPanel.executeCommand(feedbackToSystem);
    }


    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isReadModule()) {
                handleModule();
            }

            if (commandResult.isCalendarCommand()) {
                handleCalendar(commandResult.getFeedbackToSystem());
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
