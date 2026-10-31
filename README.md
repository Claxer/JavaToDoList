# TaskFlow — To-Do List Manager

TaskFlow is a simple, modern desktop To-Do List Manager built with **Java and Java Swing**. It helps users organize daily activities, manage tasks, and track their productivity through a clean and easy-to-use graphical interface.

This project is currently **under active development**, with new features, improvements, and updates being continuously added.

## Features

- **Task Management** — Add, edit, and delete tasks.
- **Task Completion** — Mark tasks as completed or pending.
- **Task Filtering** — Filter tasks by All, Pending, and Completed.
- **Task Statistics** — View total, pending, and completed task counts.
- **Task Descriptions** — Add additional information to individual tasks.
- **Local Storage** — Automatically save tasks using Java file handling.
- **Data Persistence** — Load previously saved tasks when reopening the application.
- **Dark Interface** — Clean and minimalist desktop design.

## Technologies Used

- **Java** — Main programming language.
- **Java Swing** — Graphical user interface.
- **Java File Handling** — Local task storage.
- **Object Serialization** — Saving and loading task data.
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
│
├── README.md
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

**Requirements:**
- Java Development Kit (JDK).
- Visual Studio Code or another Java-compatible IDE.

## Development Status

**Currently in Development**

TaskFlow is an ongoing personal programming project. The current version includes basic task management, task filtering, completion tracking, statistics, and local data storage.

New features, interface improvements, and functionality enhancements are continuously being developed and added to the project.

## Planned Improvements

- Task priority levels (Low, Medium, High).
- Due dates and deadlines.
- Task search functionality.
- Improved task filtering and sorting.
- Enhanced graphical interface.
- Better task organization.
- Additional productivity features.

## Project Purpose

This project was created to practice and improve Java programming skills, particularly in object-oriented programming, GUI development, file handling, and data management.

It also serves as part of my growing GitHub portfolio as an aspiring IT professional.

## License

This project is open-source and available for educational and personal use.
