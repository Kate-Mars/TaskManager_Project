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

    public void deleteTask(Scanner sc) {
        showTasks();
        if (getTasks().isEmpty()) return;

        System.out.print("Введите номер для удаления: ");
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx >= 0 && idx < getTasks().size()) {
                getTasks().remove(idx);
                System.out.println("Задача удалена");
            }
            else {
                System.out.println("Неверный номер");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ввведите корректный номер задачи");
        }
    }

    public void editTask(Scanner sc) {
        showTasks();
        if (getTasks().isEmpty()){
            return;
        }

        System.out.print("Введите номер задачи, которую вы хотите отредактировать: ");
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx < 0 || idx >= getTasks().size()) {
                System.out.println("Неверный номер");
                return;
            }

            Task task = getTasks().get(idx);

            System.out.println("Что необходимо изменить?");
            System.out.println("1 - Название");
            System.out.println("2 - Описание");
            System.out.println("3 - Дедлайн");
            System.out.println("4 - Приоритет");
            System.out.println("Ваш выбор: ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Введите новое название: ");
                    task.setTitle(sc.nextLine());
                    break;
                case "2":
                    System.out.println("Введите новое описание: ");
                    task.setDescription(sc.nextLine());
                    break;
                case "3":
                    String duration = "";
                    while (true) {
                        System.out.println("Введите новый срок (dd.MM.yyyy HH:mm): ");
                        duration = sc.nextLine();
                        if (isValidDate(duration)){
                            break;
                        }
                        System.out.println("Неверный формат даты. Попробуйте ещё раз");
                    }
                    task.setDate(duration);
                    break;
                case "4":
                    int priority = 0;
                    while (true) {
                        System.out.println("Введите новый приоритет (1-5): ");
                        try {
                            priority = Integer.parseInt(sc.nextLine());
                            if (priority >= 1 && priority <= 5){
                                break;
                            }
                            System.out.println("Приоритет - число от 1 до 5");
                        } catch (NumberFormatException e) {
                            System.out.println("Введите числовое значение от 1 до 5");
                        }
                    }
                    task.setPriority(priority);
                    break;
                default:
                    System.out.println("Неверный выбор. Необходимо выбрать опцию от 1 до 4");
                    return;
            }
            System.out.println("Задача обновлена успешно");
        } catch (NumberFormatException e) {
            System.out.println("Ввведите числовое значение");
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
