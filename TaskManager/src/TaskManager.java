import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManager {

    private final List<Task> tasks;
    public TaskManager() { this.tasks = new ArrayList<>(); }
    public List<Task> getTasks() { return tasks; }

    public void addTask(Scanner sc) {
        System.out.print("Название: ");
        String title = sc.nextLine();
        System.out.print("Описание: ");
        String description = sc.nextLine();

        String duration = "";
        while (true) {
            System.out.print("Срок (ДД.ММ.ГГГГ ЧЧ:ММ): ");
            duration = sc.nextLine();
            if (isValidDate(duration)) {
                break;
            } else {
                System.out.print("Неверный формат. Пропробуйте ещё раз\n");
            }
        }

        int priority = 0;
        while (true) {
            System.out.print("Приоритет (1-5): ");
            try {
                priority = Integer.parseInt(sc.nextLine());
                if (priority >= 1 && priority <= 5) {
                    break;
                }
                System.out.println("Приоритет - число от 1 до 5");
            } catch (NumberFormatException e) {
                System.out.println("Введите числовое значение от 1 до 5");
            }
        }

        tasks.add(new Task(title, description, duration, priority));
        System.out.println("Задача добавлена успешно!");
    }

    public void showTasks() {
        List<Task> tasks = getTasks();
        if (tasks.isEmpty()) {
            System.out.println("Задач нет");
            return;
        }
        System.out.println("Список задач: ");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    private boolean isValidDate(String date) {
        return parseDate(date) != null;
    }

    private LocalDateTime parseDate(String date) {
        String[] patterns = {
                "dd.MM.yyyy H:mm", "dd.MM.yyyy HH:mm",
                "d.M.yyyy H:mm", "d.M.yyyy HH:mm"
        };

        for (String p : patterns) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
                return LocalDateTime.parse(date, fmt);
            } catch (DateTimeParseException ignored) { }
        }
        return null;
    }
}
