package manageme.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import manageme.commons.core.index.Index;
import manageme.model.link.Link;
import manageme.model.link.LinkModule;
import manageme.model.link.UniqueLinkList;
import manageme.model.module.Module;
import manageme.model.module.UniqueModuleList;
import manageme.model.task.Task;
import manageme.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed
 */
public class ManageMe implements ReadOnlyManageMe {

    private final UniqueLinkList links;
    private final UniqueModuleList modules;
    private final UniqueTaskList tasks;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        links = new UniqueLinkList();
        modules = new UniqueModuleList();
        tasks = new UniqueTaskList();
    }

    public ManageMe() {}

    /**
     * Creates an ManageMe using the links, tasks, and modules in the {@code toBeCopied}
     */
    public ManageMe(ReadOnlyManageMe toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the link list with {@code links}.
     * {@code links} must not contain duplicate links.
     */
    public void setLinks(List<Link> links) {
        this.links.setLinks(links);
    }

    /**
     * Replaces the contents of the module list with {@code modules}.
     * {@code modules} must not contain duplicate modules.
     */
    public void setModules(List<Module> modules) {
        this.modules.setModules(modules);
    }

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Resets the existing data of this {@code ManageMe} with {@code newData}.
     */
    public void resetData(ReadOnlyManageMe newData) {
        requireNonNull(newData);

        setLinks(newData.getLinkList());
        setModules(newData.getModuleList());
        setTasks(newData.getTaskList());
    }

    //// link-level operations

    /**
     * Returns true if a link with the same identity as {@code link} exists in the ManageMe.
     */
    public boolean hasLink(Link link) {
        requireNonNull(link);
        return links.contains(link);
    }

    /**
     * Adds a link to the ManageMe.
     * The link must not already exist in the ManageMe.
     */
    public void addLink(Link p) {
        links.add(p);
    }

    /**
     * Replaces the given link {@code target} in the list with {@code editedLink}.
     * {@code target} must exist in the ManageMe.
     * The link identity of {@code editedLink} must not be the same as another existing link in the ManageMe.
     */
    public void setLink(Link target, Link editedLink) {
        requireNonNull(editedLink);

        links.setLink(target, editedLink);
    }

    /**
     * Removes {@code key} from this {@code ManageMe}.
     * {@code key} must exist in the ManageMe.
     */
    public void removeLink(Link key) {
        links.remove(key);
    }


    public void openLink(Link key) {
        links.open(key);
    }

    //// module-level operations

    /**
     * Returns true if a module with the same identity as {@code module} exists in the ManageMe.
     */
    public boolean hasModule(Module module) {
        requireNonNull(module);
        return modules.contains(module);
    }

    /**
     * Adds a module to the ManageMe.
     * The module must not already exist in the ManageMe.
     */
    public void addModule(Module p) {
        modules.add(p);
    }

    /**
     * Replaces the given module {@code target} in the list with {@code editedModule}.
     * {@code target} must exist in the ManageMe.
     * The module identity of {@code editedModule} must not be the same as another existing module in the ManageMe.
     */
    public void setModule(Module target, Module editedModule) {
        requireNonNull(editedModule);

        modules.setModule(target, editedModule);
    }

    /**
     * Removes {@code key} from this {@code ManageMe}.
     * {@code key} must exist in the ManageMe.
     */
    public void removeModule(Module key) {
        links.removeMod(key);
        modules.remove(key);
    }

    //// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the ManageMe.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    /**
     * Adds a task to the ManageMe.
     * The task must not already exist in the ManageMe.
     */
    public void addTask(Task p) {
        tasks.add(p);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the ManageMe.
     * The task identity of {@code editedTask} must not be the same as another existing task in the ManageMe.
     */
    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);
    }

    /**
     * Removes {@code key} from this {@code ManageMe}.
     * {@code key} must exist in the ManageMe.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return links.asUnmodifiableObservableList().size() + " links\n"
                + modules.asUnmodifiableObservableList().size() + " modules\n"
                + tasks.asUnmodifiableObservableList().size() + " tasks";
    }

    @Override
    public ObservableList<Link> getLinkList() {
        return links.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Module> getModuleList() {
        return modules.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public ArrayList<Task> getModifiableTaskList() {
        return tasks.asModifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ManageMe // instanceof handles nulls
                && links.equals(((ManageMe) other).links)
                && modules.equals(((ManageMe) other).modules)
                && tasks.equals(((ManageMe) other).tasks));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(links, modules, tasks);
    }
}
