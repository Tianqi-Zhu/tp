package manageme.model;

import java.nio.file.Path;

import manageme.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getManageMeFilePath();
}
