import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class TaskManager implements Serializable {
    private List<Project> projects;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;

    public TaskManager() {
        this.projects = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void start() {
        loadTasksFromFile();

        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createTask();
                    break;
                case 2:
                    listTasks();
                    break;
                case 3:
                    markTaskAsCompleted();
                    break;
                case 4:
                    setTaskReminder();
                    break;
                case 5:
                    viewStatistics();
                    break;
                case 6:
                    createProject();
                    break;
                case 7:
                    listProjects();
                    break;
                case 8:
                    saveTasksToFile();
                    break;
                case 9:
                    System.out.println("Exiting Task Manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }

    private void displayMenu() {
        System.out.println("Task Manager Menu:");
        System.out.println("1. Create Task");
        System.out.println("2. List Tasks");
        System.out.println("3. Mark Task as Completed");
        System.out.println("4. Set Task Reminder");
        System.out.println("5. View Statistics");
        System.out.println("6. Create Project");
        System.out.println("7. List Projects");
        System.out.println("8. Save Tasks to File");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private void createTask() {
        System.out.println("Enter task details:");
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Due Date (yyyy-MM-dd HH:mm:ss): ");
        String dueDateStr = scanner.nextLine();

        System.out.print("Priority (HIGH, MEDIUM, LOW): ");
        String priorityStr = scanner.nextLine();
        Priority priority = Priority.valueOf(priorityStr.toUpperCase());

        System.out.print("Set Reminder (yes/no): ");
        String reminderChoice = scanner.nextLine();
        boolean hasReminder = reminderChoice.equalsIgnoreCase("yes");

        Task newTask = new Task(title, description, dueDateStr, priority, hasReminder);

        System.out.print("Assign the task to a project (Enter project name or leave empty): ");
        String projectName = scanner.nextLine();
        if (!projectName.isEmpty()) {
            Project project = getOrCreateProject(projectName);
            project.addTask(newTask);
        } else {
            projects.get(0).addTask(newTask); // Assign to the default project
        }

        System.out.println("Task created successfully!");
    }

    private void listTasks() {
        System.out.println("List of Tasks:");

        for (Project project : projects) {
            System.out.println("Project: " + project.getProjectName());
            List<Task> projectTasks = project.getTasks();
            if (projectTasks.isEmpty()) {
                System.out.println("No tasks in this project.");
            } else {
                for (Task task : projectTasks) {
                    System.out.println(task);
                }
            }
        }
    }

    private void markTaskAsCompleted() {
        listTasks();

        System.out.print("Enter the title of the task to mark as completed: ");
        String titleToMark = scanner.nextLine();

        boolean found = false;
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (task.getTitle().equalsIgnoreCase(titleToMark)) {
                    task.markAsCompleted();
                    System.out.println("Task marked as completed!");
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            System.out.println("Task not found.");
        }
    }

    private void setTaskReminder() {
        listTasks();

        System.out.print("Enter the title of the task to set a reminder: ");
        String titleToSetReminder = scanner.nextLine();

        boolean found = false;
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (task.getTitle().equalsIgnoreCase(titleToSetReminder)) {
                    task.setHasReminder(true);
                    System.out.println("Reminder set for the task!");
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            System.out.println("Task not found.");
        }
    }

    private void viewStatistics() {
        int totalTasks = 0;
        int completedTasks = 0;
        int tasksWithReminders = 0;

        for (Project project : projects) {
            List<Task> projectTasks = project.getTasks();
            totalTasks += projectTasks.size();

            for (Task task : projectTasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                }
                if (task.isHasReminder()) {
                    tasksWithReminders++;
                }
            }
        }

        System.out.println("Task Manager Statistics:");
        System.out.println("Total Tasks: " + totalTasks);
        System.out.println("Completed Tasks: " + completedTasks);
        System.out.println("Tasks with Reminders: " + tasksWithReminders);
    }

    private void createProject() {
        System.out.print("Enter the name of the new project: ");
        String projectName = scanner.nextLine();

        if (!projectName.isEmpty()) {
            Project newProject = new Project(projectName);
            projects.add(newProject);
            System.out.println("Project created successfully!");
        } else {
            System.out.println("Project name cannot be empty. Project not created.");
        }
    }

    private void listProjects() {
        System.out.println("List of Projects:");

        for (Project project : projects) {
            System.out.println(project.getProjectName());
        }
    }

    private Project getOrCreateProject(String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }

        // If the project does not exist, create a new one
        Project newProject = new Project(projectName);
        projects.add(newProject);
        return newProject;
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tasks.ser"))) {
            outputStream.writeObject(projects);
            System.out.println("Tasks saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void loadTasksFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("tasks.ser"))) {
            projects = (List<Project>) inputStream.readObject();
            System.out.println("Tasks loaded from file successfully!");
        } catch (FileNotFoundException e) {
            // Ignore if the file does not exist yet
            projects.add(new Project("Default")); // Create a default project if no file is found
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.start();
    }
}
