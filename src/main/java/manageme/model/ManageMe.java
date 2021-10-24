package manageme.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import manageme.model.module.Module;
import manageme.model.module.UniqueModuleList;
import manageme.model.person.Person;
import manageme.model.person.UniquePersonList;
import manageme.model.task.Task;
import manageme.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class ManageMe implements ReadOnlyManageMe {

    private final UniquePersonList persons;
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
        persons = new UniquePersonList();
        modules = new UniqueModuleList();
        tasks = new UniqueTaskList();
    }

    public ManageMe() {}

    /**
     * Creates an ManageMe using the Tasks/Modules in the {@code toBeCopied}
     */
    public ManageMe(ReadOnlyManageMe toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
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

        setPersons(newData.getPersonList());
        setModules(newData.getModuleList());
        setTasks(newData.getTaskList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the ManageMe.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the ManageMe.
     * The person must not already exist in the ManageMe.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the ManageMe.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the ManageMe.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code ManageMe}.
     * {@code key} must exist in the ManageMe.
     */
    public void removePerson(Person key) {
        persons.remove(key);
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
        return persons.asUnmodifiableObservableList().size() + " persons\n"
                + modules.asUnmodifiableObservableList().size() + " modules\n"
                + tasks.asUnmodifiableObservableList().size() + " tasks";
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
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
                && persons.equals(((ManageMe) other).persons)
                && modules.equals(((ManageMe) other).modules)
                && tasks.equals(((ManageMe) other).tasks));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, modules, tasks);
    }
}
