import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Project implements Serializable {
    private String projectName;
    private List<Task> tasks;

    public Project(String projectName) {
        this.projectName = projectName;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public String getProjectName() {
        return projectName;
    }
}
