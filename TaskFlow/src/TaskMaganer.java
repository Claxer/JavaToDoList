import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> tasks;
    private final String FILE_PATH = "data/tasks.dat";

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    // Add a new task
    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    // Get all tasks
    public List<Task> getTasks() {
        return tasks;
    }

    // Edit an existing task
    public void editTask(int index, String title, String description) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);

            task.setTitle(title);
            task.setDescription(description);

            saveTasks();
        }
    }

    // Delete a task
    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveTasks();
        }
    }

    // Mark a task as completed or pending
    public void toggleTaskStatus(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);

            task.setCompleted(!task.isCompleted());

            saveTasks();
        }
    }

    // Get completed task count
    public int getCompletedCount() {
        int count = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                count++;
            }
        }

        return count;
    }

    // Get pending task count
    public int getPendingCount() {
        return tasks.size() - getCompletedCount();
    }

    // Save tasks to a local file
    public void saveTasks() {
        try {
            File file = new File(FILE_PATH);
            File parent = file.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            try (ObjectOutputStream output =
                         new ObjectOutputStream(
                                 new FileOutputStream(file))) {

                output.writeObject(tasks);
            }

        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load previously saved tasks
    @SuppressWarnings("unchecked")
    public void loadTasks() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            tasks = (List<Task>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}
