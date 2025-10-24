import jdk.jfr.Description;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    private String formatTimeDiff() {
        try {
            LocalDateTime deadline = parseDate(this.date);
            if (deadline == null) { return "Неверный формат даты"; }
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, deadline);
            long seconds = duration.getSeconds();
            if (Math.abs(seconds) < 60) { return seconds >= 0 ? "через несколько секунд" : "несколько секунд назад"; }
            long minutes = Math.abs(seconds / 60);
            long hours = Math.abs(seconds / 3600);
            long days = Math.abs(seconds / 86400);
            if (seconds >= 0) {
                if (days > 0) {
                    return String.format("через %d %s", days, getDays(days));
                } else if (hours > 0) {
                    return String.format("через %d %s", hours, getHours(hours));
                } else {
                    return String.format("через %d %s", minutes, getMinutes(minutes));
                }
            } else {
                // Дедлайн в прошлом
                if (days > 0) {
                    return String.format("%d %s назад", days, getDays(days));
                } else if (hours > 0) {
                    return String.format("%d %s назад", hours, getHours(hours));
                } else {
                    return String.format("%d %s назад", minutes, getMinutes(minutes));
                }
            }
        } catch (Exception e) {
            return "Ошибка расчета времени";
        }
    }

    private String getDays(long days) {
        if (days % 10 == 1 && days % 100 != 11) return "день";
        if (days % 10 >= 2 && days % 10 <= 4 && (days % 100 < 10 || days % 100 >= 20)) return "дня";
        return "дней";
    }

    private String getHours(long hours) {
        if (hours % 10 == 1 && hours % 100 != 11) return "час";
        if (hours % 10 >= 2 && hours % 10 <= 4 && (hours % 100 < 10 || hours % 100 >= 20)) return "часа";
        return "часов";
    }

    private String getMinutes(long minutes) {
        if (minutes % 10 == 1 && minutes % 100 != 11) return "минуту";
        if (minutes % 10 >= 2 && minutes % 10 <= 4 && (minutes % 100 < 10 || minutes % 100 >= 20)) return "минуты";
        return "минут";
    }

    private LocalDateTime parseDate(String date) {
        String[] patterns = { "dd.MM.yyyy H:mm", "dd.MM.yyyy HH:mm", "d.M.yyyy H:mm", "d.M.yyyy HH:mm" };

        for (String p : patterns) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
                return LocalDateTime.parse(date, fmt);
            } catch (DateTimeParseException ignored) { }
        }
        return null;
    }

    @Override
    public String toString() {
        String deadline = formatTimeDiff();
        return String.format(
                "Название: %s | Описание: %s | Срок: %s | Приоритет: %d | Дедлайн: %s",
                title, description, date, priority, deadline
        );
    }
}
