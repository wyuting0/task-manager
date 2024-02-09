import java.io.Serializable;

class Task implements Serializable {
    private String title;
    private String description;
    private String dueDate;
    private Priority priority;
    private boolean isCompleted;
    private boolean hasReminder;

    public Task(String title, String description, String dueDate, Priority priority, boolean hasReminder) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = false;
        this.hasReminder = hasReminder;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Description: %s, Due Date: %s, Priority: %s, Completed: %s, Reminder: %s",
                title, description, dueDate, priority, isCompleted ? "Yes" : "No", hasReminder ? "Yes" : "No");
    }
}
