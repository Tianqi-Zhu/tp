package manageme.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import manageme.commons.exceptions.IllegalValueException;
import manageme.model.task.Task;
import manageme.model.task.TaskDescription;
import manageme.model.task.TaskModule;
import manageme.model.task.TaskName;
import manageme.model.task.TaskTime;

/**
 * Jackson-friendly version of {@link Task}.
 */
public class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    // 3 Optional - time/end-time/modules
    private final String taskName;
    private final String taskDescription;
    private final String module;
    private final String start;
    private final String end;
    // private final List<JsonAdaptedTag> tagged = new ArrayList<>(); // Link to Module


    /**
     * Constructs a {@code JsonAdaptedTask} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("name") String taskName, @JsonProperty("description") String description,
                           @JsonProperty("module") String module, @JsonProperty("start") String start,
                           @JsonProperty("end") String end) {
        //                   @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.taskName = taskName;
        this.taskDescription = description;
        this.module = module;
        this.start = start;
        this.end = end;
        //if (tagged != null) {
        //    this.tagged.addAll(tagged);
        //  }
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */
    public JsonAdaptedTask(Task source) {
        this.taskName = source.getName().value;
        this.taskDescription = source.getDescription().value;
        //tagged.addAll(source.getTag().stream()
        //        .map(JsonAdaptedTag::new)
        //        .collect(Collectors.toList());
        this.module = source.getTaskModule().value;
        this.start = source.getStart().value;
        this.end = source.getEnd().value;
    }


    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task.
     */
    public Task toModelType() throws IllegalValueException {
        //final List<Tag> taskTags = new ArrayList<>();
        //for (JsonAdaptedTag tag : tagged) {
        //    taskTags.add(tag.toModelType());
        //}

        if (taskName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "TaskName"));
        }
        if (!TaskName.isValidName(taskName)) {
            throw new IllegalValueException(TaskName.MESSAGE_CONSTRAINTS);
        }
        final TaskName modelName = new TaskName(taskName);

        if (taskDescription == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Task Description"));
        }
        if (!TaskDescription.isValidDescription(taskDescription)) {
            throw new IllegalValueException(TaskDescription.MESSAGE_CONSTRAINTS);
        }
        final TaskDescription modelDescription = new TaskDescription(taskDescription);
        final TaskModule modelModule = !module.equals("") ? new TaskModule(module) : null;

        final TaskTime modelStart = !start.equals("") ? new TaskTime(start) : null;

        final TaskTime modelEnd = !end.equals("") ? new TaskTime(end) : null;

        // final Set<Tag> modelTags = new HashSet<>(taskTags);

        if (modelModule == null && modelStart == null && modelEnd == null) {
            return new Task(modelName, modelDescription);
        } else if (modelModule != null && modelStart == null && modelEnd == null) {
            return new Task(modelName, modelDescription, modelModule);
        } else if (modelModule == null && modelStart == null) {
            return new Task(modelName, modelDescription, modelEnd);
        } else if (modelModule != null && modelStart == null) {
            return new Task(modelName, modelDescription, modelModule, modelEnd);
        } else if (modelModule == null && end != null) {
            return new Task(modelName, modelDescription, modelStart, modelEnd);
        } else {
            return new Task(modelName, modelDescription, modelModule, modelStart, modelEnd);
        }
    }
}
