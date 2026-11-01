
# TaskFlow — To-Do List Manager

TaskFlow is a modern desktop To-Do List Manager built with **Java and Java Swing**. It helps users organize daily activities, manage tasks, and track their productivity through a clean, minimalist dark-themed graphical interface.

This project is currently **under active development**, with new features, improvements, and updates being continuously added.

## Features

### Task Management
- **Add Tasks** — Create new tasks with titles and descriptions.
- **Edit Tasks** — Update existing task information.
- **Delete Tasks** — Remove tasks with a confirmation prompt.
- **Task Completion** — Mark tasks as completed or pending.
- **Clear Completed Tasks** — Remove all completed tasks with confirmation.

### Task Organization
- **Task Categories** — Organize tasks into School, Work, Personal, Projects, and Others.
- **Task Priorities** — Assign Low, Medium, or High priority levels.
- **Category Filtering** — Display tasks based on their assigned categories.
- **Status Filtering** — Filter tasks by All Statuses, Pending, and Completed.
- **Task Search** — Search tasks using their titles, descriptions, or categories.
- **Task Sorting** — Arrange tasks by priority or earliest due date.

### Productivity Tracking
- **Task Statistics** — View total, pending, completed, and overdue task counts.
- **Progress Tracking** — Monitor task completion through a progress bar and percentage.
- **Due Dates** — Assign deadlines using the YYYY-MM-DD date format.
- **Overdue Detection** — Automatically identify unfinished tasks past their due dates.
- **Completion Progress** — Automatically update productivity statistics when tasks are completed.

### Data Management
- **Local Storage** — Automatically save tasks using Java file handling.
- **Data Persistence** — Load previously saved tasks when reopening the application.
- **Object Serialization** — Store task information locally using Java serialization.

### User Interface
- **Dark Interface** — Clean and minimalist dark-themed desktop design.
- **Task Cards** — Display task details, priorities, categories, deadlines, and completion status.
- **Confirmation Dialogs** — Confirm important actions such as deleting tasks and clearing completed tasks.

## Technologies Used

- **Java** — Main programming language.
- **Java Swing** — Graphical user interface.
- **Java AWT** — Interface layouts, colors, fonts, and styling.
- **Java File Handling** — Local task storage.
- **Object Serialization** — Saving and loading task data.
- **Java Collections** — Managing and organizing task lists.
- **Java Time API** — Handling due dates and overdue detection.
- **VS Code** — Development environment.

## Project Structure

```text
TaskFlow/
│
├── src/
│   ├── Main.java
│   ├── Task.java
│   ├── TaskManager.java
│   └── TaskFlowGUI.java
├── README.md
└── .gitignore
```

## How to Run

**1. Clone the repository**

```bash
git clone YOUR_REPOSITORY_URL
```

**2. Open the project folder**

```bash
cd TaskFlow
```

**3. Compile the Java files**

```bash
javac -d out src/*.java
```

**4. Run the application**

```bash
java -cp out Main
```

### Requirements

- Java Development Kit (JDK).
- Visual Studio Code or another Java-compatible IDE.
- Windows, macOS, or Linux operating system with Java Swing support.

## Development Status

**Currently in Development**

TaskFlow is an ongoing personal programming project. The current version includes:

- Complete task management functionality.
- Task priorities and categories.
- Due dates and overdue detection.
- Search, filtering, and sorting.
- Productivity statistics and progress tracking.
- Local data storage and persistence.
- A modern dark-themed graphical interface.

New features, interface improvements, and functionality enhancements are continuously being developed and added to the project.

## Planned Improvements

- Recurring tasks (Daily, Weekly, and Monthly).
- Calendar-based task management.
- More advanced task filtering.
- Improved task notifications and reminders.
- Enhanced graphical interface and animations.
- Additional productivity and organization tools.

## Project Purpose

This project was created to practice and improve Java programming skills, particularly in:

- Object-Oriented Programming (OOP).
- GUI development using Java Swing.
- File handling and object serialization.
- Data structures and collections.
- Event handling and user interaction.
- Date management and task organization.

It also serves as part of my growing GitHub portfolio as an aspiring IT professional.

## License

This project is open-source and available for educational and personal use.
