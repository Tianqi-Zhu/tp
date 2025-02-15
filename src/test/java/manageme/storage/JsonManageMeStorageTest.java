package manageme.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import manageme.commons.exceptions.DataConversionException;
import manageme.model.ManageMe;
import manageme.model.ReadOnlyManageMe;
import manageme.testutil.Assert;
import manageme.testutil.TypicalLinks;
import manageme.testutil.TypicalManageMe;

public class JsonManageMeStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyManageMe> readAddressBook(String filePath) throws Exception {
        return new JsonManageMeStorage(Paths.get(filePath)).readManageMe(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        Assert.assertThrows(DataConversionException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException() {
        Assert.assertThrows(DataConversionException.class, () -> readAddressBook("invalidLinkManageMe.json"));
    }

    @Test
    public void readAddressBook_invalidTaskAddressBook_throwDataConversionException() {
        Assert.assertThrows(DataConversionException.class, () -> readAddressBook("Task/invalidTaskManageMe.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException() {
        Assert.assertThrows(DataConversionException.class, ()
            -> readAddressBook("invalidAndValidLinkManageMe.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidTaskAddressBook_throwDataConversionException() {
        Assert.assertThrows(DataConversionException.class, ()
            -> readAddressBook("Task/invalidAndValidTaskAddressBook.json"));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        ManageMe original = TypicalManageMe.getTypicalManageMe();
        JsonManageMeStorage jsonAddressBookStorage = new JsonManageMeStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveManageMe(original, filePath);
        ReadOnlyManageMe readBack = jsonAddressBookStorage.readManageMe(filePath).get();
        assertEquals(original, new ManageMe(readBack));

        // Modify data, overwrite exiting file, and read back
        original.removeLink(TypicalLinks.LINK_B);
        jsonAddressBookStorage.saveManageMe(original, filePath);
        readBack = jsonAddressBookStorage.readManageMe(filePath).get();
        assertEquals(original, new ManageMe(readBack));

        // Save and read without specifying file path
        original.addLink(TypicalLinks.LINK_B);
        jsonAddressBookStorage.saveManageMe(original); // file path not specified
        readBack = jsonAddressBookStorage.readManageMe().get(); // file path not specified
        assertEquals(original, new ManageMe(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyManageMe addressBook, String filePath) {
        try {
            new JsonManageMeStorage(Paths.get(filePath))
                    .saveManageMe(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveAddressBook(new ManageMe(), null));
    }
}
