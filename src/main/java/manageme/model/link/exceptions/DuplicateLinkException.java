package manageme.model.link.exceptions;

public class DuplicateLinkException extends RuntimeException {
    public DuplicateLinkException() {
        super("Operation would result in duplicate links");
    }
}