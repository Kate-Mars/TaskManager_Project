import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManager {
    private final User currentUser;
    public TaskManager(User currentUser) { this.currentUser = currentUser; }
    public User getCurrentUser() { return currentUser; }
    public List<Task> getTasks() { return currentUser.getTasks(); }

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

        currentUser.addTask(new Task(title, description, duration, priority));
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
                currentUser.removeTask(idx);
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

    public void sortTasks(Scanner sc) {
        if (getTasks().isEmpty()) {
            System.out.println("Нет задач для сортировки");
            return;
        }

        System.out.println("Выберите тип сортировки: ");
        System.out.println("1. Близжайшие к дедлайну");
        System.out.println("2. Дальние к дедлайну");
        System.out.println("3. Убывание приоритета");
        System.out.println("4. Возрастание приоритета");
        System.out.println("Ваш выбор: ");

        String choice = sc.nextLine();
        List<Task> tasks = getTasks();

        switch (choice) {
            case "1":
                tasks.sort((task1, task2) -> {
                    LocalDateTime duration1 = parseDate(task1.getDate());
                    LocalDateTime duration2 = parseDate(task2.getDate());
                    if (duration1 == null || duration2 == null) {
                        return 0;
                    }
                    return duration1.compareTo(duration2);
                });
                System.out.println("Задачи успешно отсортированы по близжайшей дате к дедлайну");
                break;
            case "2":
                tasks.sort((task1, task2) -> {
                    LocalDateTime duration1 = parseDate(task1.getDate());
                    LocalDateTime duration2 = parseDate(task2.getDate());
                    if (duration1 == null || duration2 == null) return 0;
                    return duration2.compareTo(duration1);
                });
                System.out.println("Задачи успешно отсортированы по дальней дате к дедлайну");
                break;
            case "3":
                tasks.sort((task1, task2) -> Integer.compare(task2.getPriority(), task1.getPriority()));
                System.out.println("Задачи успешно отсортированы по убыванию приоритета");
                break;
            case "4":
                tasks.sort((task1, task2) -> Integer.compare(task1.getPriority(), task2.getPriority()));
                System.out.println("Задачи успешно отсортированы по возрастанию приоритета");
                break;
            default:
                System.out.println("Необходимо выбрать опцию от 1 до 4");
                return;
        }
        showTasks();
    }

    public void searchTasks(Scanner sc) {
        if (getTasks().isEmpty()) {
            System.out.println("Нет задач для поиска");
            return;
        }

        System.out.println("Искать задачи по: ");
        System.out.println("1. Названию");
        System.out.println("2. Описанию");
        System.out.println("3. Приоритету");
        System.out.print("Выбор: ");

        String choice = sc.nextLine();
        List<Task> results = new ArrayList<>();

        switch (choice) {
            case "1":
                System.out.print("Введите название для поиска: ");
                String title = sc.nextLine().toLowerCase();
                for (Task task : getTasks()) {
                    if (task.getTitle().toLowerCase().contains(title)) {
                        results.add(task);
                    }
                }
                break;
            case "2":
                System.out.print("Введите описание для поиска: ");
                String desc = sc.nextLine().toLowerCase();
                for (Task task : getTasks()) {
                    if (task.getDescription().toLowerCase().contains(desc)) {
                        results.add(task);
                    }
                }
                break;
            case "3":
                System.out.print("Введите приоритет для поиска (1-5): ");
                try {
                    int priority = Integer.parseInt(sc.nextLine());
                    for (Task task : getTasks()) {
                        if (task.getPriority() == priority) {
                            results.add(task);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Введите корректный приоритет - от 1 до 5");
                    return;
                }
                break;
            default:
                System.out.println("Неверный выбор");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("Задачи не найдены");
        } else {
            System.out.println("Найдено задач: " + results.size());
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
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
