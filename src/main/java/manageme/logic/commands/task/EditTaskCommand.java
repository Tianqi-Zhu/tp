package manageme.logic.commands.task;

import static java.util.Objects.requireNonNull;
import static manageme.logic.commands.task.AddTaskCommand.MESSAGE_START_LATER_THAN_END;
import static manageme.logic.commands.task.AddTaskCommand.MESSAGE_START_WITHOUT_END;
import static manageme.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;
import java.util.Optional;

import manageme.commons.core.Messages;
import manageme.commons.core.index.Index;
import manageme.commons.util.CollectionUtil;
import manageme.logic.commands.Command;
import manageme.logic.commands.CommandResult;
import manageme.logic.commands.exceptions.CommandException;
import manageme.model.Model;
import manageme.model.task.Task;
import manageme.model.task.TaskDescription;
import manageme.model.task.TaskModule;
import manageme.model.task.TaskName;
import manageme.model.task.TaskTime;



/**
 * Updates a task identified using it's displayed index from ManageMe.
 */
public class EditTaskCommand extends Command {

    public static final String COMMAND_WORD = "editTask";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index              of the task in the filtered task list to edit
     * @param editTaskDescriptor details to edit the task with
     */
    public EditTaskCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        if (!editedTask.getStart().isEmpty() && editedTask.getEnd().isEmpty()) {
            throw new CommandException(MESSAGE_START_WITHOUT_END);
        }

        if (!editedTask.getStart().isEmpty() && !editedTask.getEnd().isEmpty()
                && editedTask.getStart().getTime().isAfter(editedTask.getEnd().getTime())) {
            throw new CommandException(MESSAGE_START_LATER_THAN_END);
        }

        model.setTask(taskToEdit, editedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        TaskName updatedName = editTaskDescriptor.getTaskName().orElse(taskToEdit.getName());
        TaskDescription updatedDescription = editTaskDescriptor.getTaskDescription()
                .orElse(taskToEdit.getDescription());
        // Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(taskToEdit.getTags());
        TaskModule updatedModule = editTaskDescriptor.getModule()
                .orElse(taskToEdit.getTaskModule());
        TaskTime updatedStartTime = editTaskDescriptor.getStart()
                .orElse(taskToEdit.getStart());
        TaskTime updatedEndTime = editTaskDescriptor.getEnd()
                .orElse(taskToEdit.getEnd());
        return new Task(updatedName, updatedDescription, updatedModule, updatedStartTime, updatedEndTime);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTaskCommand)) {
            return false;
        }

        // state check
        EditTaskCommand e = (EditTaskCommand) other;
        return index.equals(e.index)
                && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value will replace the
     * corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private TaskName name;
        private TaskDescription description;
        private TaskModule module;
        private TaskTime start;
        private TaskTime end;
        // private Set<Tag> tags;

        public EditTaskDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setModule(toCopy.module);
            setStart(toCopy.start);
            setEnd(toCopy.end);
            // setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, module, start, end);
        }

        public void setName(TaskName name) {
            this.name = name;
        }

        public Optional<TaskName> getTaskName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(TaskDescription description) {
            this.description = description;
        }

        public Optional<TaskDescription> getTaskDescription() {
            return Optional.ofNullable(description);
        }

        public void setModule(TaskModule module) {
            this.module = module;
        }

        public Optional<TaskModule> getModule() {
            return Optional.ofNullable(module);
        }

        public void setStart(TaskTime start) {
            this.start = start;
        }

        public Optional<TaskTime> getStart() {
            return Optional.ofNullable(start);
        }

        public void setEnd(TaskTime end) {
            this.end = end;
        }

        public Optional<TaskTime> getEnd() {
            return Optional.ofNullable(end);
        }

        ///**
        // * Sets {@code tags} to this object's {@code tags}.
        // * A defensive copy of {@code tags} is used internally.
        // */
        //public void setTags(Set<Tag> tags) {
        //    this.tags = (tags != null) ? new HashSet<>(tags) : null;
        //}

        ///**
        // * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
        // * if modification is attempted.
        // * Returns {@code Optional#empty()} if {@code tags} is null.
        // */
        //public Optional<Set<Tag>> getTags() {
        //    return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        //}

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getTaskName().equals(e.getTaskName())
                    && getTaskDescription().equals(e.getTaskDescription())
                    && getModule().equals(e.getModule())
                    && getStart().equals(e.getStart())
                    && getEnd().equals(e.getEnd());
            //        && getTags().equals(e.getTags());
        }
    }
}
