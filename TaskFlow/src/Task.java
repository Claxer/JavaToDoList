import java.io.Serializable;

public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private boolean completed;
    private String priority;
    private String dueDate;
    private String category;

    public Task(String title, String description) {
        this(title, description, "Medium", "", "Personal");
    }

    public Task(String title, String description,
                String priority, String dueDate) {
        this(title, description, priority, dueDate, "Personal");
    }

    public Task(String title, String description,
                String priority, String dueDate, String category) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.priority = priority;
        this.dueDate = dueDate;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getPriority() {
        return priority == null ? "Medium" : priority;
    }

    public String getDueDate() {
        return dueDate == null ? "" : dueDate;
    }

    public String getCategory() {
        return category == null || category.isBlank()
                ? "Personal" : category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return title;
    }
}
