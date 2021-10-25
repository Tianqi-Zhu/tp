package manageme.model.link;

import java.util.Objects;

import manageme.commons.util.CollectionUtil;

/**
 * Represents a link in the app.
 * Guarantees: field values are validated, immutable
 */
public class Link {
    private final LinkName name;
    private final LinkAddress address;
    private final LinkModule module;
    private final LinkTask task;

    /**
     * Basic Link with only name and address.
     */
    public Link(LinkName name, LinkAddress address) {
        CollectionUtil.requireAllNonNull(name, address);
        this.name = name;
        this.address = address;
        this.module = LinkModule.empty();
        this.task = LinkTask.empty();
    }

    /**
     * Basic Link associated with a module.
     */
    public Link(LinkName name, LinkAddress address, LinkModule module, LinkTask task) {
        CollectionUtil.requireAllNonNull(name, address);
        this.name = name;
        this.address = address;
        this.module = module;
        this.task = task;
    }

    public LinkName getName() {
        return name;
    }

    public LinkAddress getAddress() {
        return address;
    }

    public LinkModule getLinkModule() {
        return module;
    }

    public LinkTask getLinkTask() {
        return task;
    }

    /**
     * Returns true if both links have the same link address.
     * This defines a weaker notion of equality between two links.
     * @param otherLink
     */
    public boolean isSameLink(Link otherLink) {
        if (otherLink == this) {
            return true;
        }

        return otherLink != null
                && otherLink.getAddress().equals(getAddress());
    }

    /**
     * Oen link in web browser.
     */
    public void open() {
        address.open();
    }

    /**
     * Returns true if both mods have the same identity and data fields.
     * This defines a stronger notion of equality between two mods.
     * @param other
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Link)) {
            return false;
        }

        Link otherLink = (Link) other;
        return otherLink.getName().equals(getName())
                && otherLink.getAddress().equals(getAddress())
                && otherLink.getLinkModule().equals(getLinkModule())
                && otherLink.getLinkTask().equals(getLinkTask());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, module);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Address: ")
                .append(getAddress())
                .append("; LinkModule: ")
                .append(getLinkModule());
        return builder.toString();
    }
}
