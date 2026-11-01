import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskFlowGUI extends JFrame {

    private final Color BACKGROUND = new Color(18, 18, 18);
    private final Color PANEL = new Color(30, 30, 30);
    private final Color CARD = new Color(38, 38, 38);
    private final Color WHITE = new Color(240, 240, 240);
    private final Color GRAY = new Color(160, 160, 160);
    private final Color ACCENT = new Color(90, 150, 255);
    private final Color RED = new Color(235, 90, 90);
    private final Color ORANGE = new Color(240, 170, 70);
    private final Color GREEN = new Color(90, 190, 130);

    private final String[] CATEGORIES = {
            "School", "Work", "Personal", "Projects", "Others"
    };

    private TaskManager taskManager;

    private JPanel taskContainer;
    private JPanel statisticsPanel;
    private JProgressBar progressBar;

    private JTextField searchField;
    private JComboBox<String> filterBox;
    private JComboBox<String> sortBox;
    private JComboBox<String> categoryFilterBox;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel completedLabel;
    private JLabel overdueLabel;
    private JLabel progressLabel;

    public TaskFlowGUI() {
        taskManager = new TaskManager();

        setTitle("TaskFlow - To-Do List Manager");
        setSize(1150, 850);
        setMinimumSize(new Dimension(950, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);

        createInterface();
        refreshTasks();
    }

    private void createInterface() {
        setLayout(new BorderLayout(15, 15));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(BACKGROUND);

        JLabel title = new JLabel("TaskFlow");
        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel subtitle = new JLabel("Organize your work. Track your progress.");
        subtitle.setForeground(GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        JButton addButton = createButton("+ Add Task", ACCENT);
        addButton.addActionListener(e -> addTask());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        // Statistics
        statisticsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        statisticsPanel.setBackground(BACKGROUND);

        totalLabel = new JLabel("0");
        pendingLabel = new JLabel("0");
        completedLabel = new JLabel("0");
        overdueLabel = new JLabel("0");

        statisticsPanel.add(createStatCard("Total Tasks", totalLabel));
        statisticsPanel.add(createStatCard("Pending", pendingLabel));
        statisticsPanel.add(createStatCard("Completed", completedLabel));
        statisticsPanel.add(createStatCard("Overdue", overdueLabel));

        // Progress
        JPanel progressPanel = new JPanel(new BorderLayout(10, 8));
        progressPanel.setBackground(PANEL);
        progressPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        progressLabel = new JLabel("Completion Progress: 0%");
        progressLabel.setForeground(WHITE);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 13));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(GREEN);
        progressBar.setBackground(CARD);

        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        // Search and filters
        JPanel controlPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        controlPanel.setBackground(BACKGROUND);

        searchField = new JTextField();
        searchField.setBackground(PANEL);
        searchField.setForeground(WHITE);
        searchField.setCaretColor(WHITE);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD),
                new EmptyBorder(10, 10, 10, 10)
        ));
        searchField.setToolTipText("Search tasks by title, description, or category");

        filterBox = new JComboBox<>(
                new String[]{"All Statuses", "Pending", "Completed"}
        );

        sortBox = new JComboBox<>(
                new String[]{
                        "Default Order",
                        "Priority: High to Low",
                        "Due Date: Earliest First"
                }
        );

        categoryFilterBox = new JComboBox<>();
        categoryFilterBox.addItem("All Categories");

        for (String category : CATEGORIES) {
            categoryFilterBox.addItem(category);
        }

        styleComboBox(filterBox);
        styleComboBox(sortBox);
        styleComboBox(categoryFilterBox);

        JButton clearButton = createButton("Clear Completed", RED);
        clearButton.addActionListener(e -> clearCompletedTasks());

        controlPanel.add(searchField);
        controlPanel.add(filterBox);
        controlPanel.add(sortBox);
        controlPanel.add(categoryFilterBox);
        controlPanel.add(clearButton);
        controlPanel.add(new JLabel());
        controlPanel.add(new JLabel());
        controlPanel.add(new JLabel());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BACKGROUND);

        topPanel.add(header);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(statisticsPanel);
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(progressPanel);
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(controlPanel);

        // Task list
        taskContainer = new JPanel();
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));
        taskContainer.setBackground(BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(taskContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                refreshTasks();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshTasks();
            }

            public void changedUpdate(DocumentEvent e) {
                refreshTasks();
            }
        });

        filterBox.addActionListener(e -> refreshTasks());
        sortBox.addActionListener(e -> refreshTasks());
        categoryFilterBox.addActionListener(e -> refreshTasks());
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(title);
        heading.setForeground(GRAY);
        heading.setFont(new Font("Arial", Font.PLAIN, 12));

        valueLabel.setForeground(WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 25));

        card.add(heading, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 15, 10, 15));

        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(PANEL);
        comboBox.setForeground(WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(4, 20);
        JComboBox<String> priorityBox = new JComboBox<>(
                new String[]{"Low", "Medium", "High"}
        );
        JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
        JTextField dueDateField = new JTextField();

        priorityBox.setSelectedItem("Medium");
        categoryBox.setSelectedItem("Personal");

        JPanel panel = createTaskForm(
                titleField, descriptionArea,
                priorityBox, categoryBox, dueDateField
        );

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add New Task",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        String category = (String) categoryBox.getSelectedItem();
        String dueDate = dueDateField.getText().trim();

        if (title.isEmpty()) {
            showMessage("Please enter a task title.");
            return;
        }

        if (!isValidDate(dueDate)) {
            showMessage("Invalid date. Use YYYY-MM-DD.");
            return;
        }

        Task task = new Task(
                title, description, priority, dueDate, category
        );

        taskManager.addTask(task);
        refreshTasks();
    }

    private void editTask(int index) {
        if (index < 0 || index >= taskManager.getTasks().size()) return;

        Task task = taskManager.getTasks().get(index);

        JTextField titleField = new JTextField(task.getTitle());
        JTextArea descriptionArea = new JTextArea(
                task.getDescription(), 4, 20
        );

        JComboBox<String> priorityBox = new JComboBox<>(
                new String[]{"Low", "Medium", "High"}
        );

        JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
        JTextField dueDateField = new JTextField(task.getDueDate());

        priorityBox.setSelectedItem(task.getPriority());

        if (categoryBox.getItemCount() > 0) {
            categoryBox.setSelectedItem(task.getCategory());
        }

        JPanel panel = createTaskForm(
                titleField, descriptionArea,
                priorityBox, categoryBox, dueDateField
        );

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Task",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        String category = (String) categoryBox.getSelectedItem();
        String dueDate = dueDateField.getText().trim();

        if (title.isEmpty()) {
            showMessage("Task title cannot be empty.");
            return;
        }

        if (!isValidDate(dueDate)) {
            showMessage("Invalid date. Use YYYY-MM-DD.");
            return;
        }

        taskManager.updateTaskDetails(
                index, title, description, priority, dueDate, category
        );

        refreshTasks();
    }

    private JPanel createTaskForm(
            JTextField titleField,
            JTextArea descriptionArea,
            JComboBox<String> priorityBox,
            JComboBox<String> categoryBox,
            JTextField dueDateField) {

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setPreferredSize(new Dimension(380, 380));

        panel.add(new JLabel("Task Title"));
        panel.add(titleField);

        panel.add(new JLabel("Description"));
        panel.add(new JScrollPane(descriptionArea));

        panel.add(new JLabel("Priority"));
        panel.add(priorityBox);

        panel.add(new JLabel("Category"));
        panel.add(categoryBox);

        panel.add(new JLabel("Due Date (YYYY-MM-DD, optional)"));
        panel.add(dueDateField);

        return panel;
    }

    private boolean isValidDate(String date) {
        if (date.isEmpty()) return true;

        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void deleteTask(int index) {
        if (index < 0 || index >= taskManager.getTasks().size()) return;

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            taskManager.deleteTask(index);
            refreshTasks();
        }
    }

    private void clearCompletedTasks() {
        if (taskManager.getCompletedCount() == 0) {
            showMessage("There are no completed tasks to clear.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete all completed tasks?",
                "Clear Completed Tasks",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            taskManager.clearCompletedTasks();
            refreshTasks();
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void refreshTasks() {
        taskContainer.removeAll();

        List<Task> displayedTasks =
                new ArrayList<>(taskManager.getTasks());

        // Search
        String keyword = searchField.getText().trim();

        if (!keyword.isEmpty()) {
            displayedTasks.retainAll(taskManager.searchTasks(keyword));
        }

        // Status filter
        String status = (String) filterBox.getSelectedItem();

        if ("Pending".equals(status)) {
            displayedTasks.removeIf(Task::isCompleted);
        } else if ("Completed".equals(status)) {
            displayedTasks.removeIf(task -> !task.isCompleted());
        }

        // Category filter
        String category = (String) categoryFilterBox.getSelectedItem();

        if (category != null && !category.equals("All Categories")) {
            displayedTasks.removeIf(
                    task -> !task.getCategory().equalsIgnoreCase(category)
            );
        }

        // Sorting
        String sort = (String) sortBox.getSelectedItem();

        if ("Priority: High to Low".equals(sort)) {
            displayedTasks.sort(Comparator.comparingInt(task -> {
                switch (task.getPriority().toLowerCase()) {
                    case "high": return 1;
                    case "medium": return 2;
                    case "low": return 3;
                    default: return 4;
                }
            }));
        } else if ("Due Date: Earliest First".equals(sort)) {
            displayedTasks.sort((task1, task2) -> {
                String date1 = task1.getDueDate();
                String date2 = task2.getDueDate();

                if (date1.isEmpty()) return 1;
                if (date2.isEmpty()) return -1;

                return date1.compareTo(date2);
            });
        }

        // Statistics
        totalLabel.setText(String.valueOf(taskManager.getTasks().size()));
        pendingLabel.setText(String.valueOf(taskManager.getPendingCount()));
        completedLabel.setText(String.valueOf(taskManager.getCompletedCount()));
        overdueLabel.setText(String.valueOf(taskManager.getOverdueCount()));

        int progress = (int) taskManager.getCompletionPercentage();

        progressBar.setValue(progress);
        progressLabel.setText("Completion Progress: " + progress + "%");

        if (displayedTasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasks found.");
            emptyLabel.setForeground(GRAY);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            emptyLabel.setBorder(new EmptyBorder(30, 10, 10, 10));

            taskContainer.add(emptyLabel);
        }

        for (Task task : displayedTasks) {
            int originalIndex = taskManager.getTasks().indexOf(task);

            taskContainer.add(
                    createTaskCard(task, originalIndex)
            );

            taskContainer.add(Box.createVerticalStrut(10));
        }

        taskContainer.revalidate();
        taskContainer.repaint();
    }

    private JPanel createTaskCard(Task task, int index) {
        JPanel card = new JPanel(new BorderLayout(10, 8));
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBackground(CARD);

        JLabel titleLabel = new JLabel(task.getTitle());

        titleLabel.setForeground(
                task.isCompleted() ? GRAY : WHITE
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));

        JLabel descriptionLabel = new JLabel(
                task.getDescription().isEmpty()
                        ? "No description"
                        : task.getDescription()
        );

        descriptionLabel.setForeground(GRAY);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel informationLabel = new JLabel(
                "Priority: " + task.getPriority()
                        + "   |   Category: " + task.getCategory()
        );

        informationLabel.setForeground(getPriorityColor(task.getPriority()));
        informationLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel dateLabel = new JLabel(
                task.getDueDate().isEmpty()
                        ? "No due date"
                        : "Due: " + task.getDueDate()
        );

        dateLabel.setForeground(GRAY);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        if (taskManager.isOverdue(task)) {
            dateLabel.setText("OVERDUE - " + task.getDueDate());
            dateLabel.setForeground(RED);
        }

        JLabel statusLabel = new JLabel(
                task.isCompleted() ? "Completed" : "Pending"
        );

        statusLabel.setForeground(
                task.isCompleted() ? GREEN : ORANGE
        );

        details.add(titleLabel);
        details.add(Box.createVerticalStrut(5));
        details.add(descriptionLabel);
        details.add(Box.createVerticalStrut(8));
        details.add(informationLabel);
        details.add(Box.createVerticalStrut(5));
        details.add(dateLabel);
        details.add(Box.createVerticalStrut(5));
        details.add(statusLabel);

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 5, 5)
        );

        actions.setBackground(CARD);

        JCheckBox completedBox = new JCheckBox("Done");
        completedBox.setSelected(task.isCompleted());
        completedBox.setBackground(CARD);
        completedBox.setForeground(WHITE);

        completedBox.addActionListener(e -> {
            taskManager.setTaskCompleted(
                    index, completedBox.isSelected()
            );
            refreshTasks();
        });

        JButton editButton = createButton("Edit", ACCENT);
        editButton.addActionListener(e -> editTask(index));

        JButton deleteButton = createButton("Delete", RED);
        deleteButton.addActionListener(e -> deleteTask(index));

        actions.add(completedBox);
        actions.add(editButton);
        actions.add(deleteButton);

        card.add(details, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        return card;
    }

    private Color getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high":
                return RED;
            case "medium":
                return ORANGE;
            case "low":
                return GREEN;
            default:
                return WHITE;
        }
    }
}
