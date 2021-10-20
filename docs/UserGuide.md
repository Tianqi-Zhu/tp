---
layout: page
title: User Guide
---

ManageMe is a **desktop app for time management and resource organisation, optimized for use via a Command Line Interface (CLI)** while still having the benefits of a Graphical User Interface (GUI). If you can type fast, ManageMe can get your tasks done faster than traditional GUI apps.

# Table of Contents
1. Features
    1. Tasks
        1. Add a Task: `addTask`
        2. Read a Task: `readTask`
        3. Edit a Task's Details: `editTask`
        4. Delete a Task: `deleteTask`
    2. Modules
        1. Add a Module: `addMod`
        2. Read a Module: `readMod`
        3. Update a Module's Details: `editMod`
        4. Delete a Module: `deleteMod`
        5. Find a Module by keyword: `findMod`
        6. List all Modules: `listMod`
    3. Calendar
    4. Others
        1. Get help: `help`
        2. Exit program: `exit`
2. Command Summary

--------------------------------------------------------------------------------------------------------------------

## UI Mockup:

![Ui](images/Ui1.png)

![Ui](images/Ui2.png)

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user. E.g. `addTask n/TASK_NAME`
  , here `DESCRIPTION` is the parameter.<br>

* Items in square brackets are optional. E.g. `addTask n/TASK_NAME [mod/CS2103]`<br>

</div>

--------------------------------------------------------------------------------------------------------------------
### TASK:

#### Adding a task: `addTask`

Adds a task to the address book.
A name and description for the task is compulsory.
It is optional to include an associated Module name, a start datetime and an end datetime.
**:information_source: A start datetime cannot be included without an end datetime:**

Format:

    addTask n/NAME d/DESCRIPTION [mod/MODULE_NAME] [s/START_DATETIME] [e/END_DATETIME]

#### Read details of a task: `readTask`
View a task in detail.

Format: `readTask INDEX`
- Read task details at the specified `INDEX`. The index refers to the index number shown in the displayed task list. 
- The index **must be a positive integer** 1, 2, 3, ...
- Tasks will be displayed in this format: [Status] description (Module)(Date Time)
    - Status: X for done, blank for not done
    - Module is the name of the associated module
    - Date is in the format: Month Day Year
    - Time is in 24-hour format

Example: `readTask 3`

#### Edit a task: editTask
Edit an existing task in the task list.

Format: `editTask INDEX [n/NAME] [d/DESCRIPTION] [mod/MODULE_NAME] [s/START_DATETIME] [e/END_DATETIME]`
* The index refers to the index number shown in the displayed module list
* The index **must be a positive integer** 1, 2, 3, ...
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Example: `editTask 3 d/buy milk`

#### Deleting a task: `deleteTask`
Deletes the specified task from the task list.

Format: `deleteTask INDEX`
- Deletes the task at the specified `INDEX`
- The index refers to the index number shown in the displayed task list
- The index **must be a positive integer** 1, 2, 3, ...

Example: `deleteTask 2`

### MODULE:
#### Adding a module: `addMod`
Adds a module into the module list. A module contains its name and zoom links. It can also be associated with any
number of Todo, Deadline, Event tasks (like exams, assignments). These Tasks are added in the Task tab with a `/mod
MODULE`.

Format: `addMod NAME /l LINK_NAME LINK` <br/>
Examples: `addMod CS2103 /l tutorial https://...`

#### Read details of a module: `readMod`
View module in detail. Creates a pop-up window to show the course name, link, and all tasks associated with the course.

Format: `readMod INDEX`<br/>
* The index refers to the index number shown in the displayed module list
* The index **must be a positive integer** 1, 2, 3, ...

Examples: `readMod 2`

#### Edit a module: `editMod`
Update an existing module in the mod list.

Format: `editMod INDEX [n/NAME] [l/LINK]`
* Deletes the mod by the specified `INDEX`.<br/>
* The index refers to the index number shown in the displayed module list
* The index **must be a positive integer** 1, 2, 3, ...
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Example: `editMod 2 n/CS2103T l/https://...`

#### Delete a module: `deleteMod`
Deletes the specified mod from the mod list.

Format: `deleteMod INDEX`
* Deletes the mod by the specified `INDEX`.
* The index refers to the index number shown in the displayed module list
* The index **must be a positive integer** 1, 2, 3, ...

Example: `deleteMod 2` deletes module No.2 from the list.

#### Find a module by keyword: `findMod`
Finds modules whose names contain any of the given keywords.

Format: `findMod KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g hans will match Hans
* The order of the keywords does not matter. e.g. Hans Bo will match Bo Hans
* Only the name is searched.
* Only full words will be matched e.g. Han will not match Hans
* Modules matching at least one keyword will be returned (i.e. OR search).

Example: `findMod computer` returns `Computer Organization` and `Computer Architecture`.

#### List all modules : `listMod`
Display the full list of modules. This command is used to return to the full list
of modules after searching for specific modules.

Format: `listMod`

### CALENDAR:

More commands to come in further versions.

### OTHERS:

#### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

#### Exiting the program : `exit`

Exits the program.

Format: `exit`

#### Saving the data

Data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

## Command summary

Action | Format, Examples
--------|------------------
**AddTask** | `addTask n/NAME d/DESCRIPTION [mod/MODULE_NAME] [s/START_DATETIME] [e/END_DATETIME]` e.g., `addTask n/Do Assignment d/Refer to Lectures 7-9 /mod CS2100 s/2021-10-05T12:00 e/2021-10-07T23:59`
**ReadTask** | `readTask INDEX`<br>e.g., `readTask 3`
**EditTask** | `editTask INDEX [n/NAME] [d/DESCRIPTION] [mod/MODULE_NAME] [s/START_DATETIME] [e/END_DATETIME]`<br>e.g., `editTask 3 d/buy milk`
**DeleteTask** | `deleteTask INDEX`<br>e.g., `deleteTask 3`
**AddModule** | `addMod NAME /l LINK_NAME LINK`<br>e.g.,`addMod CS2103 /l tutorial https://...`
**ReadModule** | `readMod INDEX`<br>e.g., `readMod 2`
**EditModule** | `editMod INDEX [n/NAME] [l/LINK]`<br>e.g., `editMod 2 n/CS2103T l/https://...`
**DeleteModule** | `deleteMod INDEX`<br>e.g., `deleteMod 2`
**FindModule** | `findMod KEYWORD [MORE_KEYWORDS]`<br>e.g., `findMod computer`
**ListModule** | `listMod`
**Help** | `help`
**Exit** | `exit`
