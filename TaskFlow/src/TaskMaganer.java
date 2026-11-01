import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManager {

    private List<Task> tasks;
    private final String FILE_PATH = "data/tasks.dat";

    private final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void editTask(int index, String newTitle,
                         String newDescription) {
        if (!isValidIndex(index)) return;

        Task task = tasks.get(index);
        task.setTitle(newTitle);
        task.setDescription(newDescription);

        saveTasks();
    }

    // Keeps compatibility with the previous GUI.
    public void updateTaskDetails(int index, String title,
                                  String description, String priority,
                                  String dueDate) {
        updateTaskDetails(index, title, description,
                priority, dueDate, tasks.get(index).getCategory());
    }

    // Updated method with category support.
    public void updateTaskDetails(int index, String title,
                                  String description, String priority,
                                  String dueDate, String category) {
        if (!isValidIndex(index)) return;

        Task task = tasks.get(index);

        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setCategory(category);

        saveTasks();
    }

    public void deleteTask(int index) {
        if (!isValidIndex(index)) return;

        tasks.remove(index);
        saveTasks();
    }

    public void toggleTaskStatus(int index) {
        if (!isValidIndex(index)) return;

        Task task = tasks.get(index);
        task.setCompleted(!task.isCompleted());

        saveTasks();
    }

    public void setTaskCompleted(int index, boolean completed) {
        if (!isValidIndex(index)) return;

        tasks.get(index).setCompleted(completed);
        saveTasks();
    }

    public int getCompletedCount() {
        int count = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                count++;
            }
        }

        return count;
    }

    public int getPendingCount() {
        return tasks.size() - getCompletedCount();
    }

    public double getCompletionPercentage() {
        if (tasks.isEmpty()) return 0;

        return (double) getCompletedCount()
                / tasks.size() * 100;
    }

    public int getOverdueCount() {
        int count = 0;

        for (Task task : tasks) {
            if (isOverdue(task)) {
                count++;
            }
        }

        return count;
    }

    public boolean isOverdue(Task task) {
        if (task.isCompleted() || task.getDueDate().isBlank()) {
            return false;
        }

        try {
            LocalDate dueDate =
                    LocalDate.parse(task.getDueDate(), DATE_FORMAT);

            return dueDate.isBefore(LocalDate.now());

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public List<Task> getTasksByPriority(String priority) {
        List<Task> result = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                result.add(task);
            }
        }

        return result;
    }

    // Filter tasks by category.
    public List<Task> getTasksByCategory(String category) {
        List<Task> result = new ArrayList<>();

        for (Task task : tasks) {
            if (category.equalsIgnoreCase("All")
                    || task.getCategory().equalsIgnoreCase(category)) {
                result.add(task);
            }
        }

        return result;
    }

    // Search by title, description, or category.
    public List<Task> searchTasks(String keyword) {
        List<Task> result = new ArrayList<>();

        String search = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(search)
                    || task.getDescription().toLowerCase().contains(search)
                    || task.getCategory().toLowerCase().contains(search)) {
                result.add(task);
            }
        }

        return result;
    }

    public List<Task> getTasksSortedByPriority() {
        List<Task> sorted = new ArrayList<>(tasks);

        sorted.sort(Comparator.comparingInt(task -> {
            switch (task.getPriority().toLowerCase()) {
                case "high":
                    return 1;
                case "medium":
                    return 2;
                case "low":
                    return 3;
                default:
                    return 4;
            }
        }));

        return sorted;
    }

    public List<Task> getTasksSortedByDueDate() {
        List<Task> sorted = new ArrayList<>(tasks);

        sorted.sort((task1, task2) -> {
            String date1 = task1.getDueDate();
            String date2 = task2.getDueDate();

            if (date1.isBlank()) return 1;
            if (date2.isBlank()) return -1;

            return date1.compareTo(date2);
        });

        return sorted;
    }

    public void clearCompletedTasks() {
        tasks.removeIf(Task::isCompleted);
        saveTasks();
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

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

    @SuppressWarnings("unchecked")
    public void loadTasks() {
        File file = new File(FILE_PATH);

        if (!file.exists()) return;

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {

            Object data = input.readObject();

            if (data instanceof List<?>) {
                tasks.clear();

                for (Object item : (List<?>) data) {
                    if (item instanceof Task) {
                        tasks.add((Task) item);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}
