package manageme.model;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import manageme.model.module.Module;
import manageme.model.person.Person;
import manageme.model.task.Task;


/**
 * Unmodifiable view of a ManageMe
 */
public interface ReadOnlyManageMe {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the modules list.
     * This list will not contain any duplicate modules.
     */
    ObservableList<Module> getModuleList();

    /**
     * Returns an unmodifiable view of the tasks list.
     * This list will not contain any duplicate tasks.
     */
    ObservableList<Task> getTaskList();

    /**
     * Returns an Modifiable view of the tasks list just for .
     * This list will not contain any duplicate tasks.
     */
    ArrayList<Task> getModifiableTaskList();

}
