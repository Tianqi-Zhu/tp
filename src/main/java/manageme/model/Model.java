package manageme.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import manageme.commons.core.GuiSettings;
import manageme.commons.core.index.Index;
import manageme.model.link.Link;
import manageme.model.link.LinkModule;
import manageme.model.module.Module;
import manageme.model.task.Task;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Link> PREDICATE_SHOW_ALL_LINKS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Module> PREDICATE_SHOW_ALL_MODULES = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Task> PREDICATE_SHOW_ALL_TASKS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' ManageMe file path.
     */
    Path getManageMeFilePath();

    /**
     * Sets the user prefs' ManageMe file path.
     */
    void setManageMeFilePath(Path manageMeFilePath);

    /**
     * Replaces ManageMe data with the data in {@code manageMe}.
     */
    void setManageMe(ReadOnlyManageMe manageMe);

    /** Returns the ManageMe */
    ReadOnlyManageMe getManageMe();

    /**
     * Returns true if a link with the same identity as {@code link} exists in the ManageMe.
     */
    boolean hasLink(Link link);

    /**
     * Deletes the given link.
     * The link must exist in the ManageMe.
     */
    void deleteLink(Link target);

    /**
     * Opens the given link.
     * The link must exist in the ManageMe.
     */
    void openLink(Link target);

    /**
     * Adds the given link.
     * {@code link} must not already exist in the ManageMe.
     */
    void addLink(Link link);

    /**
     * Replaces the given link {@code target} with {@code editedLink}.
     * {@code target} must exist in the ManageMe.
     * The link identity of {@code editedLink} must not be the same as another existing link in the ManageMe.
     */
    void setLink(Link target, Link editedLink);

    /** Returns an unmodifiable view of the filtered link list */
    ObservableList<Link> getFilteredLinkList();

    /**
     * Updates the filter of the filtered link list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredLinkList(Predicate<Link> predicate);

    /**
     * Returns true if a module with the same identity as {@code module} exists in the ManageMe.
     */
    boolean hasModule(Module module);

    /**
     * Deletes the given module.
     * The module must exist in the ManageMe.
     */
    void deleteModule(Module target);

    /**
     * Adds the given module.
     * {@code module} must not already exist in the ManageMe.
     */
    void addModule(Module module);

    /**
     * Replaces the given module {@code target} with {@code editedModule}.
     * {@code target} must exist in the ManageMe.
     * The module identity of {@code editedModule} must not be the same as another existing module in the ManageMe.
     */
    void setModule(Module target, Module editedModule);

    /** Returns an unmodifiable view of the filtered module list */
    ObservableList<Module> getFilteredModuleList();

    /**
     * Updates the filter of the filtered module list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredModuleList(Predicate<Module> predicate);

    /**
     * Returns the module to be read.
     */
    Optional<Module> getReadModule();

    /**
     * Updates the module to be read to {@code module}.
     */
    void setReadModule(Module module);

    /**
     * Returns true if a task with the same identity as {@code task} exists in the ManageMe.
     */
    boolean hasTask(Task task);

    /**
     * Deletes the given task.
     * The task must exist in the ManageMe.
     */
    void deleteTask(Task target);

    /**
     * Adds the given task.
     * {@code task} must not already exist in the ManageMe.
     */
    void addTask(Task task);

    /**
     * Replaces the given task {@code target} with {@code editedtask}.
     * {@code target} must exist in the ManageMe.
     * The task identity of {@code editedtask} must not be the same as another existing task in the ManageMe.
     */
    void setTask(Task target, Task editedTask);

    /** Returns an unmodifiable view of the filtered task list */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTaskList(Predicate<Task> predicate);

    /** Returns an unmodifiable view of the unfiltered task list */
    ObservableList<Task> getUnfilteredTaskList();

    /** Returns an unmodifiable view of the unfiltered link list */
    ObservableList<Link> getUnfilteredLinkList();

    Link deleteModLink(LinkModule module, Index targetIndex);
}
