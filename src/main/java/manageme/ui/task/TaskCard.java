package manageme.ui.task;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import manageme.model.task.Task;
import manageme.ui.UiPart;

/**
 * An UI component that displays information of a {@code Task}.
 */
public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Task task;

    @FXML
    private VBox cardPane;
    @FXML
    private Label taskName;
    @FXML
    private Label taskId;
    @FXML
    private Label taskDescription;
    @FXML
    private Label isDone;
    @FXML
    private Label taskModule;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;

    /**
     * Creates a {@code taskCode} with the given {@code task} and index to display.
     */
    public TaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        taskId.setText(displayedIndex + ". ");
        taskName.setText(task.getName().value);
        taskDescription.setText(task.getDescription().value);
        isDone.setText("[" + (task.isDone()) + "]");
        taskModule.setText(task.getTaskModule().value);
        startTime.setText(task.getStart().isEmpty() ? "" : "Start Time: " + task.getStart().value);
        endTime.setText(task.getEnd().isEmpty() ? "" : "End Time: " + task.getEnd().value);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return taskId.getText().equals(card.taskId.getText())
                && task.equals(card.task);
    }
}
