---
layout: page
title: Developer Guide
---
* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

A Digital-Age student who...
* Needs to organize multiple tasks (events, tasks and deadlines), some of them involve computer files stored locally.
* Needs to organize multiple modules with many links to relevant online resources.
* Prefers visualizing all tasks and modules on a calendar for clear and quick presentation.
* Prefers desktop application over mobile application.
* Prefers typing over the use of mouse.
* Comfortable with the use of Command Line Interface (CLI).

**Value proposition**:
* Manage modules and tasks faster than a typical mouse/GUI driven app.
* Manage multiple web links and file paths associated with modules and tasks.
* Summarises crucial information clearly such as:
    1) Tasks due Today and
    2) Upcoming Events on the Calendar



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                 | I want to …​                                                       | So that I can…​                                                                                        |
| -------- | ------------------------------------------ | --------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| `* * *`  | Forgetful User                             | Add tasks                                                             | I can keep track of what I have done, and need to do.                                                     |
| `* * *`  | Forgetful User                             | Add optional begin and end times to tasks                             |                                                                                                           |
| `* * *`  | Forgetful User                             | Mark a task as done                                                   |                                                                                                           |
| `* * *`  | Forgetful User                             | View all my tasks                                                     |                                                                                                           |
| `* *`    | Forgetful User                             | Add optional tags to tasks                                            | I can organize tasks by their types.                                                                      |
| `* * *`  | Forgetful User                             | Edit my tasks                                                         | I can make changes to the status of the tasks or change task information.                                 |
| `* * *`  | Forgetful User                             | Delete my tasks                                                       | I can get rid of unwanted tasks.                                                                          |
| `* *`    | Forgetful User                             | Add recurring tasks                                                   | I do not have to add the same task multiple times.                                                        |
| `* *`    | Easily-distracted User                     | Filter tasks by tags                                                  | I can choose to see only the most important tasks.                                                        |
| `* * *`  | Student                                    | Add modules                                                           | I can store all relevant information and links related to a module together.                              |
| `* * *`  | Student                                    | View all my modules                                                   | I can keep track of the different modules that I am taking this semester.                                 |
| `* * *`  | Student                                    | Edit my modules                                                       | I can make changes to information related to a module.                                                    |
| `* * *`  | Student                                    | Delete my modules                                                     | I can get rid of modules that I am no longer taking.                                                      |
| `* * *`  | Busy user                                  | View calendar                                                         | I can check the dates easily.                                                                             |
| `* * *`  | Busy Student                               | See all tasks for today when I open the app                           | I can know what happens today conveniently.                                                               |
| `* * *`  | Busy Student                               | View all tasks in a single day in the calendar according to its date  | I can know what happens on any particular date.                                                           |
| `* * *`  | Busy Student                               | View all upcoming tasks in a calendar                                 | I do not have to look through my entire task list to find upcoming tasks.                                 |
| `* * *`  | Busy Student                               | See tasks in a calendar                                               | I can view the tasks in chronological order.                                                              |
| `* * *`  | Busy Student                               | See classes in a calendar                                             | I can view my classes conveniently.                                                                       |
| `* *`    | Digital-Age Student                        | Add filepaths in my local directory to tasks                          | I can access assignment files related to a task directly from the application.                            |
| `* *`    | Digital Age Student                        | Add web links to tasks                                                | I can access related links conveniently.                                                                  |
| `* *`    | Digital-Age Student                        | Add filepaths in my local directory to modules                        | I can access module related files directly from the application.                                          |
| `* *`    | Digital Age Student                        | Add web links to modules                                              | I can access related links conveniently.                                                                  |
| `* *`    | Easily Distracted Student                  | Receive reminders for tasks that have upcoming deadlines              | I have sufficient time to complete my 'due-soon' tasks should I get distracted by other tasks.            |
| `* * *`  | User                                       | Search for a task using keywords                                      | I do not have to waste time browsing through my entire task list to find a task.                          |
| `* `     | User                                       | Search for a link using keywords                                      | I do not have to waste time opening module or task tab to find a link.                                    |
| `*`      | User                                       | Search everywhere in the app                                          | I can search anything I want with minimum commands.                                                       |
| `* *`    | New User                                   | Open the command summary to help me navigate the application          | I know how to use the application to perform the functions that I want.                                   |
| `* *`    | Experienced User                           | Close the command summary                                             | It does not take up unnecessary screen space after I am familiar with the commands.                        |
| `*`      | User who likes to customize                | Personalise the appearance of the application                         | I can change the appearance of the application to my liking.                                              |
| `* *`    | Long-term user                             | Clear all tasks and modules                                           | I can easily reset the app when a new semester starts.                                                    |
| `*`      | Long-term user                             | Archive all the data                                                  | I can find my old tasks and module when I need to.                                                        |
| `*`      | Long-term user                             | Load previous data                                                    | I can read my old tasks and module when I need to.                                                        |  


### Use cases

(For all use cases below, the **System** is `ManageMe` and the **Actor** is the `User`, unless specified otherwise)

**Use case: Add Task**

**MSS:**
1. User requests to add a task together with its details.
2. System adds the new task and displays it. <br>
   Use case ends.

**Extensions:**
* 1a. System detects an error in the entered data.
    * 1a1. System shows error in parsing data.
      Use case resumes from step 1.


**Use case: Edit Task**

**MSS:**
1. User requests to edit a specified task and enters the new details.
2. System updates the details and displays the edited task. <br>
   Use case ends.

**Extensions:**
* 1a. User enters an invalid task index<br>
    * 1a1. System shows error in reading index.
      Use case resumes from step 1.
* 1b. System detects an error in the entered data.
    * 1a1. System shows error in parsing data.
      Use case resumes from step 1. <br><br>


**Use case: Delete Task**

**MSS:**

1. User requests to delete a specified task.
2. System removes the specified task from the task list.
3. System updates the GUI to show the new task list. <br>
   Use case ends.

**Extension:**

* 1a. User enters an invalid task index
    * 1a1. System shows error in reading index.
      Use case resumes from step 1.


**Use case: Add Module**

**MSS:**

1. User requests to add a module together with its details.
2. System adds the new module and displays it.
3. Use case ends.

**Extensions**
* 1a. The parameters entered are invalid, such as Invalid Module Code, mismatch of parameters.
    * 1a1. System shows error in reading index.
      Use case resumes from step 1.

**Use case: Edit Module**

**MSS:**

1. User requests to edit a specified module and enters the new details.
2. System updates the details and displays the edited module. <br>
   Use case ends.

**Extensions:**
* 1a. User enters an invalid module index<br>
    * 1a1. System shows error in reading index.
      Use case resumes from step 1.
* 1b. System detects format errors in the entered data.
    * 1a1. System shows error in format.
      Use case resumes from step 1. <br><br>


**Use case: Delete Module**

**MSS:**

1. User requests to delete a specified module.
2. System removes the specified module from the module list.
3. System updates the GUI to show the new module list. <br>
   Use case ends.

**Extension:**

* 1a. User enters an invalid module index<br>
    * 1a1. System shows error in reading index.
      Use case resumes from step 1.


**Use case: Find Module**

**MSS:**

1. User requests to search for specific modules by keyword.
3. System updates the GUI to show a list of all modules whose name contains the keyword. <br>
   Use case ends.

**Extension:**

* 1a. User does not enter a keyword. <br>
    * 1a1. System shows invalid format error.
      Use case resumes from step 1.

**Use case: List all Modules**

**MSS:**

1. User requests to list all modules.
3. System updates the GUI to show the full list of keywords. <br>
   Use case ends.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system will not be responsible for checking the validity of external links entered into the system, such as Zoom links or Luminus links. They must be checked beforehand by the user.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Digital-age:** A post-pandemic world where many schools have switched to online teaching and learning.
* **Module:** A course taken by a student, typically in a university.
* **Online resources:** Websites used by many schools in the post-pandemic world for teaching, file submission and assessment. For example, Zoom, Luminus, Exemplify etc.
* **Command Line Interface (CLI):** An interface where the user primarily gives command to the computer by typing text lines, instead of clicking and dragging graphic components.
* **Mainstream OS:** Windows, Linux OS-X

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
