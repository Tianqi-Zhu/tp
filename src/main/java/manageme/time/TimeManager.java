package manageme.time;

import java.time.LocalDateTime;
import java.util.ArrayList;

import manageme.model.ReadOnlyManageMe;
import manageme.model.task.Task;


public class TimeManager implements Time {
    private ArrayList<Task> allTasks;


    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public TimeManager(ReadOnlyManageMe manageMe) {
        this.allTasks = manageMe.getModifiableTaskList();
    }

    /**
     * Searches the current sorted task list for any tasks that may begin or end at the current time
     */
    public void checkAlarm() {
        for (Task task: allTasks) {
            // Addition function to set how much time before actual task that you want the reminder to pop out.
            LocalDateTime time = task.getFirstOccurrence();
            LocalDateTime end = task.getEnd().getTime();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(time) && !now.isAfter(end)) {
            //    System.out.println("POP OUT! " + task.getName().value);
            }
        }
    }

    /**
     * Updates the current list of tasks based on the newest model. This is called whenver changes to model is made.
     * @param manageMe is the latest version of manageMe with the update tasks.
     */
    @Override
    public void updateTasks(ReadOnlyManageMe manageMe) {
        this.allTasks = manageMe.getModifiableTaskList();
    }

    /**
     * Starts a asynchronous thread that checks the task list every X seconds.
     * Whenever a task is due, a pop-up is launched.
     */
    @Override
    public void startTime() {
        new Thread(() -> {
            while (true) {
                try {
                    checkAlarm();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Error with timer.");
                    break;
                }
            }
        }).start();
    }
}
