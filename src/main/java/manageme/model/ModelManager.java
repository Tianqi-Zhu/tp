package manageme.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import manageme.commons.core.GuiSettings;
import manageme.commons.core.LogsCenter;
import manageme.commons.core.index.Index;
import manageme.commons.util.CollectionUtil;
import manageme.model.link.Link;
import manageme.model.link.LinkModule;
import manageme.model.module.Module;
import manageme.model.task.Task;

/**
 * Represents the in-memory model of the ManageMe data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ManageMe manageMe;
    private final UserPrefs userPrefs;
    private final FilteredList<Link> filteredLinks;
    private final FilteredList<Link> unfilteredLinks;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<Task> unfilteredTasks;
    private final FilteredList<Module> filteredModules;
    private final ArrayList<Task> modifiableUnfilteredTasks;
    private Optional<Module> readModule;

    /**
     * Initializes a ModelManager with the given manageMe and userPrefs.
     */
    public ModelManager(ReadOnlyManageMe manageMe, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(manageMe, userPrefs);

        logger.fine("Initializing with ManageMe: " + manageMe + " and user prefs " + userPrefs);

        this.manageMe = new ManageMe(manageMe);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredLinks = new FilteredList<>(this.manageMe.getLinkList());
        unfilteredLinks = new FilteredList<>(this.manageMe.getLinkList());
        filteredTasks = new FilteredList<>(this.manageMe.getTaskList());
        unfilteredTasks = new FilteredList<>(this.manageMe.getTaskList());
        filteredModules = new FilteredList<>(this.manageMe.getModuleList());
        readModule = Optional.ofNullable(null);

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
    public boolean hasLink(Link link) {
        requireNonNull(link);
        return manageMe.hasLink(link);
    }

    @Override
    public void deleteLink(Link target) {
        manageMe.removeLink(target);
    }

    @Override
    public void openLink(Link target) {
        requireNonNull(target);
        manageMe.openLink(target);
    }

    @Override
    public void addLink(Link link) {
        manageMe.addLink(link);
        updateFilteredLinkList(PREDICATE_SHOW_ALL_LINKS);
    }

    @Override
    public void setLink(Link target, Link editedLink) {
        CollectionUtil.requireAllNonNull(target, editedLink);

        manageMe.setLink(target, editedLink);
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

    /**
     * Returns an unmodifiable view of the list of {@code Link} backed by the internal list of
     * {@code versionedManageMe}
     */
    @Override
    public ObservableList<Link> getFilteredLinkList() {
        return filteredLinks;
    }

    @Override
    public void updateFilteredLinkList(Predicate<Link> predicate) {
        requireNonNull(predicate);
        filteredLinks.setPredicate(predicate);
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
    public Optional<Module> getReadModule() {
        return readModule;
    }

    @Override
    public void setReadModule(Module module) {
        requireNonNull(module);
        readModule = Optional.of(module);
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
    public ObservableList<Link> getUnfilteredLinkList() {
        return unfilteredLinks;
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
                && filteredLinks.equals(other.filteredLinks)
                && filteredModules.equals(other.filteredModules)
                && readModule.equals(other.readModule)
                && filteredTasks.equals(other.filteredTasks)
                && unfilteredTasks.equals(other.unfilteredTasks);
    }

    @Override
    public Link deleteModLink(LinkModule mod, Index i) {
        System.out.println(unfilteredLinks.size());
        FilteredList<Link> modLinks = unfilteredLinks.filtered(link -> {
            Optional<String> linkModule = link.getLinkModule().moduleName;
            if (linkModule.isEmpty()) {
                return false;
            }
            return mod.value.equals(linkModule.get());
        });
        Link toRemove = modLinks.get(i.getZeroBased());
        manageMe.removeLink(toRemove);
        return toRemove;
    }
}
