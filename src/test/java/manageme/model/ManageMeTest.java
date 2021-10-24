package manageme.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manageme.logic.commands.CommandTestUtil;
import manageme.logic.commands.task.TaskCommandTestUtil;
import manageme.model.module.Module;
import manageme.model.module.exceptions.DuplicateModuleException;
import manageme.model.person.Person;
import manageme.model.person.exceptions.DuplicatePersonException;
import manageme.model.task.Task;
import manageme.model.task.exceptions.DuplicateTaskException;
import manageme.testutil.Assert;
import manageme.testutil.ModuleBuilder;
import manageme.testutil.PersonBuilder;
import manageme.testutil.TaskBuilder;
import manageme.testutil.TypicalManageMe;
import manageme.testutil.TypicalModules;
import manageme.testutil.TypicalPersons;
import manageme.testutil.TypicalTasks;

public class ManageMeTest {

    private final ManageMe manageMe = new ManageMe();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), manageMe.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> manageMe.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        ManageMe newData = TypicalManageMe.getTypicalManageMe();
        manageMe.resetData(newData);
        assertEquals(newData, manageMe);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        List<Person> newPersons = Arrays.asList(TypicalPersons.ALICE, editedAlice);
        ManageMePersonStub newData = new ManageMePersonStub(newPersons);

        Assert.assertThrows(DuplicatePersonException.class, () -> manageMe.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateModules_throwsDuplicateModuleException() {
        // Two modules with the same identity fields
        Module editedModuleA = new ModuleBuilder(TypicalModules.MODULE_A).withLink(CommandTestUtil.VALID_LINK_ZOOM)
                .build();
        List<Module> newModules = Arrays.asList(TypicalModules.MODULE_A, editedModuleA);
        ManageMeModuleStub newData = new ManageMeModuleStub(newModules);

        Assert.assertThrows(DuplicateModuleException.class, () -> manageMe.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateTasks_throwsDuplicateTaskException() {
        // Two tasks with the same identity fields
        Task editedTaskA = new TaskBuilder(TypicalTasks.TASK_A).withDescription(TaskCommandTestUtil.VALID_DESCRIPTION_A)
                .build();
        List<Task> newTasks = Arrays.asList(TypicalTasks.TASK_A, editedTaskA);
        ManageMeTaskStub newData = new ManageMeTaskStub(newTasks);

        Assert.assertThrows(DuplicateTaskException.class, () -> manageMe.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> manageMe.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(manageMe.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        manageMe.addPerson(TypicalPersons.ALICE);
        assertTrue(manageMe.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        manageMe.addPerson(TypicalPersons.ALICE);
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        assertTrue(manageMe.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> manageMe.getPersonList().remove(0));
    }

    @Test
    public void hasModule_nullModule_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> manageMe.hasModule(null));
    }

    @Test
    public void hasModule_moduleNotInAddressBook_returnsFalse() {
        assertFalse(manageMe.hasModule(TypicalModules.MODULE_A));
    }

    @Test
    public void hasModule_moduleInAddressBook_returnsTrue() {
        manageMe.addModule(TypicalModules.MODULE_A);
        assertTrue(manageMe.hasModule(TypicalModules.MODULE_A));
    }

    @Test
    public void hasModule_moduleWithSameIdentityFieldsInAddressBook_returnsTrue() {
        manageMe.addModule(TypicalModules.MODULE_A);
        Module editedModule = new ModuleBuilder(TypicalModules.MODULE_A).withLink(CommandTestUtil.VALID_LINK_ZOOM)
                .build();
        assertTrue(manageMe.hasModule(editedModule));
    }

    @Test
    public void getModuleList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> manageMe.getModuleList().remove(0));
    }

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> manageMe.hasTask(null));
    }

    @Test
    public void hasTask_taskNotInAddressBook_returnsFalse() {
        assertFalse(manageMe.hasTask(TypicalTasks.TASK_A));
    }

    @Test
    public void hasTask_taskInAddressBook_returnsTrue() {
        manageMe.addTask(TypicalTasks.TASK_A);
        assertTrue(manageMe.hasTask(TypicalTasks.TASK_A));
    }

    @Test
    public void hasTask_taskWithSameIdentityFieldsInAddressBook_returnsTrue() {
        manageMe.addTask(TypicalTasks.TASK_A);
        Task editedTask = new TaskBuilder(TypicalTasks.TASK_A).withDescription(TaskCommandTestUtil.VALID_DESCRIPTION_A)
                .withModule(TaskCommandTestUtil.VALID_MODULE_A).build();
        assertTrue(manageMe.hasTask(editedTask));
    }

    @Test
    public void getTaskList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> manageMe.getTaskList().remove(0));
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class ManageMePersonStub implements ReadOnlyManageMe {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        private final ObservableList<Module> modules = FXCollections.observableArrayList();

        private final ObservableList<Task> tasks = FXCollections.observableArrayList();

        private final ArrayList<Task> modifiableTasks = new ArrayList<>();

        ManageMePersonStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Module> getModuleList() {
            return modules;
        }

        @Override
        public ObservableList<Task> getTaskList() {
            return tasks;
        }

        @Override
        public ArrayList<Task> getModifiableTaskList() {
            return modifiableTasks;
        }
    }

    /**
     * A stub ReadOnlyAddressBook whose Modules list can violate interface constraints.
     */
    private static class ManageMeModuleStub implements ReadOnlyManageMe {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        private final ObservableList<Module> modules = FXCollections.observableArrayList();

        private final ObservableList<Task> tasks = FXCollections.observableArrayList();

        private final ArrayList<Task> modifiableTasks = new ArrayList<>();

        ManageMeModuleStub(Collection<Module> modules) {
            this.modules.setAll(modules);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Module> getModuleList() {
            return modules;
        }

        @Override
        public ObservableList<Task> getTaskList() {
            return tasks;
        }

        @Override
        public ArrayList<Task> getModifiableTaskList() {
            return modifiableTasks;
        }
    }

    /**
     * A stub ReadOnlyAddressBook whose Tasks list can violate interface constraints.
     */
    private static class ManageMeTaskStub implements ReadOnlyManageMe {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        private final ObservableList<Module> modules = FXCollections.observableArrayList();

        private final ObservableList<Task> tasks = FXCollections.observableArrayList();

        private final ArrayList<Task> modifiableTasks = new ArrayList<>();

        ManageMeTaskStub(Collection<Task> tasks) {
            this.tasks.setAll(tasks);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Module> getModuleList() {
            return modules;
        }

        @Override
        public ObservableList<Task> getTaskList() {
            return tasks;
        }

        @Override
        public ArrayList<Task> getModifiableTaskList() {
            return modifiableTasks;
        }
    }

}
