import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("    To-Do List    ");

        UserManager userManager = new UserManager();
        userManager.loadFromFile();

        User currentUser = userManager.login(sc);
        if (currentUser == null) {
            System.out.println("Не удалось войти в систему");
            return;
        }

        TaskManager manager = new TaskManager(currentUser);

        while (true) {
            System.out.println("\n1. Добавить задачу");
            System.out.println("2. Показать задачи");
            System.out.println("3. Удалить задачу");
            System.out.println("4. Изменить задачу");
            System.out.println("5. Сортировать задачи");
            System.out.println("6. Поиск задач");
            System.out.println("7. Выход");
            System.out.print("Выберите действие: ");
            String cmd = sc.nextLine();
            switch (cmd) {
                case "1": manager.addTask(sc); break;
                case "2": manager.showTasks(); break;
                case "3": manager.deleteTask(sc); break;
                case "4": manager.editTask(sc); break;
                case "5": manager.sortTasks(sc); break;
                case "6": manager.searchTasks(sc); break;
                case "7":
                    userManager.saveToFile();
                    System.out.println("Данные сохранены. До свидания!");
                    return;
                default: System.out.println("Неверный ввод");
            }
        }
    }
}
