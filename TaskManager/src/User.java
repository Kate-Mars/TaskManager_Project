import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Task> tasks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new ArrayList<>();
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<Task> getTasks() { return tasks; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }
}
