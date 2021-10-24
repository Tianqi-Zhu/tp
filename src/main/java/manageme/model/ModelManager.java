package manageme.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import manageme.commons.core.GuiSettings;
import manageme.commons.core.LogsCenter;
import manageme.commons.util.CollectionUtil;
import manageme.model.module.Module;
import manageme.model.person.Person;
import manageme.model.task.Task;

/**
 * Represents the in-memory model of the ManageMe data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ManageMe manageMe;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Module> filteredModules;
    private final FilteredList<Module> readModule;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<Task> unfilteredTasks;
    private final ArrayList<Task> modifiableUnfilteredTasks;

    /**
     * Initializes a ModelManager with the given manageMe and userPrefs.
     */
    public ModelManager(ReadOnlyManageMe manageMe, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(manageMe, userPrefs);

        logger.fine("Initializing with ManageMe: " + manageMe + " and user prefs " + userPrefs);

        this.manageMe = new ManageMe(manageMe);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.manageMe.getPersonList());
        filteredModules = new FilteredList<>(this.manageMe.getModuleList());
        readModule = new FilteredList<>(this.manageMe.getModuleList());
        filteredTasks = new FilteredList<>(this.manageMe.getTaskList());
        unfilteredTasks = new FilteredList<>(this.manageMe.getTaskList());

        // Time Manager use
        modifiableUnfilteredTasks = new ArrayList<Task>(this.manageMe.getModifiableTaskList());
    }

    public ModelManager() {
        this(new ManageMe(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getManageMeFilePath() {
        return userPrefs.getManageMeFilePath();
    }

    @Override
    public void setManageMeFilePath(Path manageMeFilePath) {
        requireNonNull(manageMeFilePath);
        userPrefs.setManageMeFilePath(manageMeFilePath);
    }

    //=========== ManageMe ================================================================================

    @Override
    public void setManageMe(ReadOnlyManageMe manageMe) {
        this.manageMe.resetData(manageMe);
    }

    @Override
    public ReadOnlyManageMe getManageMe() {
        return manageMe;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return manageMe.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        manageMe.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        manageMe.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        CollectionUtil.requireAllNonNull(target, editedPerson);

        manageMe.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasModule(Module module) {
        requireNonNull(module);
        return manageMe.hasModule(module);
    }

    @Override
    public void deleteModule(Module target) {
        manageMe.removeModule(target);
    }

    @Override
    public void addModule(Module module) {
        manageMe.addModule(module);
        updateFilteredModuleList(PREDICATE_SHOW_ALL_MODULES);
    }

    @Override
    public void setModule(Module target, Module editedModule) {
        CollectionUtil.requireAllNonNull(target, editedModule);

        manageMe.setModule(target, editedModule);
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return manageMe.hasTask(task);
    }

    @Override
    public void deleteTask(Task target) {
        manageMe.removeTask(target);
    }

    @Override
    public void addTask(Task task) {
        manageMe.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
    }

    @Override
    public void setTask(Task target, Task editedTask) {
        CollectionUtil.requireAllNonNull(target, editedTask);

        manageMe.setTask(target, editedTask);
    }



    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedManageMe}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return filteredModules;
    }

    @Override
    public void updateFilteredModuleList(Predicate<Module> predicate) {
        requireNonNull(predicate);
        filteredModules.setPredicate(predicate);
    }

    @Override
    public ObservableList<Module> getReadModuleList() {
        return readModule;
    }

    @Override
    public void updateReadModuleList(Predicate<Module> predicate) {
        requireNonNull(predicate);
        readModule.setPredicate(predicate);
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return filteredTasks;
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    @Override
    public ObservableList<Task> getUnfilteredTaskList() {
        return unfilteredTasks;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return manageMe.equals(other.manageMe)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons)
                && filteredModules.equals(other.filteredModules)
                && readModule.equals(other.readModule)
                && filteredTasks.equals(other.filteredTasks)
                && unfilteredTasks.equals(other.unfilteredTasks);
    }

}
