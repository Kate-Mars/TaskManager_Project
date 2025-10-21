import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TaskManager {

    public void addTask(Scanner sc) {
        System.out.print("Название: ");
        String title = sc.nextLine();
        System.out.print("Описание: ");
        String descript = sc.nextLine();

        String dur = "";
        while (true) {
            System.out.print("Срок (ДД.ММ.ГГГГ ЧЧ:ММ): ");
            dur = sc.nextLine();
            if (isValidDate(dur)) {
                break;
            }
            else {
                System.out.print("Неверный формат. Пропробуйте ещё раз");
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
