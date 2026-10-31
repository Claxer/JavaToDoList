import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TaskFlowGUI extends JFrame {

    private TaskManager taskManager;

    private JPanel taskListPanel;
    private JLabel totalLabel;
    private JLabel completedLabel;
    private JLabel pendingLabel;

    private String currentFilter = "All";

    private final Color BACKGROUND = new Color(24, 24, 27);
    private final Color PANEL = new Color(39, 39, 42);
    private final Color CARD = new Color(48, 48, 52);
    private final Color WHITE = new Color(245, 245, 245);
    private final Color MUTED = new Color(161, 161, 170);
    private final Color GREEN = new Color(34, 197, 94);

    public TaskFlowGUI() {

        taskManager = new TaskManager();

        setTitle("TaskFlow - To-Do List Manager");
        setSize(950, 700);
        setMinimumSize(new Dimension(750, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);

        createInterface();
        refreshTasks();
    }

    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BACKGROUND);

        JLabel title = new JLabel("TaskFlow");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel("Organize your day. Get things done.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton addButton = new JButton("+ Add Task");
        styleButton(addButton, GREEN, Color.BLACK);
        addButton.addActionListener(e -> addTask());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        // Statistics
        JPanel statistics = new JPanel(new GridLayout(1, 3, 15, 0));
        statistics.setBackground(BACKGROUND);

        totalLabel = createStatCard(statistics, "TOTAL TASKS", "0");
        pendingLabel = createStatCard(statistics, "PENDING", "0");
        completedLabel = createStatCard(statistics, "COMPLETED", "0");

        // Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BACKGROUND);

        String[] filters = {"All", "Pending", "Completed"};

        for (String filter : filters) {

            JButton filterButton = new JButton(filter);
            styleButton(filterButton, PANEL, WHITE);

            filterButton.addActionListener(e -> {
                currentFilter = filter;
                refreshTasks();
            });

            filterPanel.add(filterButton);
        }

        // Task list
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(BACKGROUND);

        JPanel topContent = new JPanel();
        topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));
        topContent.setBackground(BACKGROUND);

        topContent.add(statistics);
        topContent.add(Box.createVerticalStrut(25));
        topContent.add(filterPanel);

        contentPanel.add(topContent, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("TaskFlow | Stay productive.");
        footer.setForeground(MUTED);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createStatCard(JPanel parent, String title, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(15, 18, 15, 18));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        valueLabel.setForeground(WHITE);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        parent.add(card);

        return valueLabel;
    }

    private void styleButton(JButton button, Color background, Color foreground) {

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
    }

    // Add a new task
    private void addTask() {

        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        panel.add(new JLabel("Task Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create New Task",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a task title.");
                return;
            }

            taskManager.addTask(new Task(title, description));

            currentFilter = "All";
            refreshTasks();
        }
    }

    // Edit a task
    private void editTask(int index) {

        Task task = taskManager.getTasks().get(index);

        JTextField titleField = new JTextField(task.getTitle());
        JTextArea descriptionArea = new JTextArea(task.getDescription(), 4, 20);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        panel.add(new JLabel("Task Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Task",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title cannot be empty.");
                return;
            }

            taskManager.editTask(index, title, description);
            refreshTasks();
        }
    }

    // Delete a task
    private void deleteTask(int index) {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {

            taskManager.deleteTask(index);
            refreshTasks();
        }
    }

    // Refresh task list and statistics
    private void refreshTasks() {

        taskListPanel.removeAll();

        List<Task> tasks = taskManager.getTasks();

        totalLabel.setText(String.valueOf(tasks.size()));
        pendingLabel.setText(String.valueOf(taskManager.getPendingCount()));
        completedLabel.setText(String.valueOf(taskManager.getCompletedCount()));

        boolean found = false;

        for (int i = 0; i < tasks.size(); i++) {

            Task task = tasks.get(i);

            if (currentFilter.equals("Pending") && task.isCompleted()) {
                continue;
            }

            if (currentFilter.equals("Completed") && !task.isCompleted()) {
                continue;
            }

            found = true;

            taskListPanel.add(createTaskCard(task, i));
            taskListPanel.add(Box.createVerticalStrut(12));
        }

        if (!found) {

            JLabel emptyLabel = new JLabel("No tasks found. Add a new task!");
            emptyLabel.setForeground(MUTED);
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            taskListPanel.add(Box.createVerticalStrut(30));
            taskListPanel.add(emptyLabel);
        }

        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    // Create individual task card
    private JPanel createTaskCard(Task task, int index) {

        JPanel card = new JPanel(new BorderLayout(15, 5));
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(15, 18, 15, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox completedBox = new JCheckBox();
        completedBox.setSelected(task.isCompleted());
        completedBox.setBackground(CARD);
        completedBox.setFocusPainted(false);

        completedBox.addActionListener(e -> {

            taskManager.toggleTaskStatus(index);
            refreshTasks();
        });

        JPanel information = new JPanel();
        information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));
        information.setBackground(CARD);

        JLabel title = new JLabel(task.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(task.isCompleted() ? MUTED : WHITE);

        if (task.isCompleted()) {
            title.setText("✓ " + task.getTitle());
        }

        JLabel description = new JLabel(
                task.getDescription().isEmpty()
                        ? "No description"
                        : task.getDescription()
        );

        description.setFont(new Font("SansSerif", Font.PLAIN, 12));
        description.setForeground(MUTED);

        JLabel status = new JLabel(
                task.isCompleted() ? "Completed" : "Pending"
        );

        status.setFont(new Font("SansSerif", Font.PLAIN, 11));
        status.setForeground(task.isCompleted() ? GREEN : MUTED);

        information.add(title);
        information.add(Box.createVerticalStrut(5));
        information.add(description);
        information.add(Box.createVerticalStrut(5));
        information.add(status);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actions.setBackground(CARD);

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        styleButton(editButton, PANEL, WHITE);
        styleButton(deleteButton, PANEL, new Color(248, 113, 113));

        editButton.addActionListener(e -> editTask(index));
        deleteButton.addActionListener(e -> deleteTask(index));

        actions.add(editButton);
        actions.add(deleteButton);

        card.add(completedBox, BorderLayout.WEST);
        card.add(information, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        return card;
    }
}
