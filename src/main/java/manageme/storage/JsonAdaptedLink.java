package manageme.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import manageme.commons.exceptions.IllegalValueException;
import manageme.model.link.Link;
import manageme.model.link.LinkAddress;
import manageme.model.link.LinkModule;
import manageme.model.link.LinkName;
import manageme.model.link.LinkTask;

/**
 * Jackson-friendly version of {@link Link}.
 */
public class JsonAdaptedLink {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Link's %s field is missing!";

    private final String linkName;
    private final String address;
    private final String module;
    private final String task;


    /**
     * Constructs a {@code JsonAdaptedLink} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedLink(@JsonProperty("name") String linkName, @JsonProperty("address") String address,
                           @JsonProperty("module") String module, @JsonProperty("task") String task) {
        this.linkName = linkName;
        this.address = address;
        this.module = module;
        this.task = task;
    }

    /**
     * Converts a given {@code Link} into this class for Jackson use.
     */
    public JsonAdaptedLink(Link source) {
        this.linkName = source.getName().value;
        this.address = source.getAddress().value;
        this.module = source.getLinkModule().value;
        this.task = source.getLinkTask().value;
    }


    /**
     * Converts this Jackson-friendly adapted link object into the model's {@code Link} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted link.
     */
    public Link toModelType() throws IllegalValueException {

        if (linkName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "LinkName"));
        }
        if (!LinkName.isValidLinkName(linkName)) {
            throw new IllegalValueException(LinkName.MESSAGE_CONSTRAINTS);
        }
        final LinkName modelName = new LinkName(linkName);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Link Description"));
        }
        if (!LinkAddress.isValidLinkAddress(address)) {
            throw new IllegalValueException(LinkAddress.MESSAGE_CONSTRAINTS);
        }
        final LinkAddress modelAddress = new LinkAddress(address);

        final LinkModule modelModule = !module.equals("") ? new LinkModule(module) : null;
        final LinkTask modelTask = !task.equals("") ? new LinkTask(task) : null;

//        if (modelModule == null) {
//            if (modelTask == null) {
//                return new Link(modelName, modelAddress);
//            } else {
//                return new Link()
//            }
//        } else {
    //}
        return new Link(modelName, modelAddress, modelModule, modelTask);

    }
}
