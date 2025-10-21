import jdk.jfr.Description;

public class Task {
    private String title;
    private String description;
    private String date;
    private int priority;

    public Task(String title, String description, String date, int priority){
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public int getPriority() { return priority; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setPriority(int priority) { this.priority = priority; }
}
