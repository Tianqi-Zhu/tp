package manageme.model.task;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manageme.commons.util.CollectionUtil;
import manageme.model.task.exceptions.DuplicateTaskException;
import manageme.model.task.exceptions.TaskNotFoundException;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 * A task is considered unique by comparing using {@code Task#isSameTask(Task)}. As such, adding and
 * updating of
 * tasks uses Task#isSameTask(task) for equality so as to ensure that the task being added or updated is
 * unique in terms of identity in the UniqueTaskList. However, the removal of a task uses Task#equals(Object) so
 * as to ensure that the task with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#isSameTask(Task)
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    private final ObservableList<Task> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    private ArrayList<Task> sortedList = new ArrayList<>();

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(Task toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTask);
    }

    /**
     * Adds a task to the list.
     * The task must not already exist in the list.
     */
    public void add(Task toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
        sortedList.add(toAdd);
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the list.
     * The task identity of {@code editedTask} must not be the same as another existing task in the list.
     */
    public void setTask(Task target, Task editedTask) {
        CollectionUtil.requireAllNonNull(target, editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        if (!target.isSameTask(editedTask) && contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        internalList.set(index, editedTask);
        if (!sortedList.contains(target)) {
            sortedList.add(editedTask);
        } else {
            sortedList.set(index, editedTask);
        }
    }


    /**
     * Removes the equivalent task from the list.
     * The task must exist in the list.
     */
    public void remove(Task toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TaskNotFoundException();
        }

        sortedList.remove(toRemove);
    }

    public void setTasks(UniqueTaskList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
        sortedList = new ArrayList<>();
        sortedList.addAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        CollectionUtil.requireAllNonNull(tasks);
        if (!tasksAreUnique(tasks)) {
            throw new DuplicateTaskException();
        }

        internalList.setAll(tasks);
        sortedList = new ArrayList<>();
        sortedList.addAll(tasks);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Task> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns the backing list as an modifiable {@code ObservableList}.
     */
    public ArrayList<Task> asModifiableObservableList() {
        sortTasksByTime();
        return sortedList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && internalList.equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code tasks} contains only unique tasks.
     */
    private boolean tasksAreUnique(List<Task> tasks) {
        for (int i = 0; i < tasks.size() - 1; i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                if (tasks.get(i).isSameTask(tasks.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sorts the tasks by their first occurrence, be it start or end date. This also removes tasks without any
     * dates specified.
     */
    private void sortTasksByTime() {
        Comparator<Task> comparator = Comparator.comparing(Task::getFirstOccurrence);
        sortedList.removeIf(task -> task.getStart().value.equals("") && task.getEnd().value.equals(""));
        sortedList.removeIf(task -> task.isDone().value);
        sortedList.sort(comparator);
    }
}
