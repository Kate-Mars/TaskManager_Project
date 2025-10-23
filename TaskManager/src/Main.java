import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("    To-Do List    ");
        UserManager userManager = new UserManager();
        userManager.loadFromFile();
        while (true) {
            User currentUser = userManager.login(sc);
            if (currentUser == null) {
                userManager.saveToFile();
                System.out.println("Выход из приложения. До свидания!");
                break;
            }
            TaskManager manager = new TaskManager(currentUser);
            boolean exitToAuth = false;
            while (!exitToAuth) {
                System.out.println("\n=== Главное меню ===");
                System.out.println("Текущий пользователь: " + currentUser.getUsername());
                System.out.println("1. Добавить задачу");
                System.out.println("2. Показать задачи");
                System.out.println("3. Удалить задачу");
                System.out.println("4. Изменить задачу");
                System.out.println("5. Сортировать задачи");
                System.out.println("6. Поиск задач");
                System.out.println("7. Выход из аккаунта");
                System.out.println("8. Выход из приложения");
                System.out.print("Выберите действие: ");
                String cmd = sc.nextLine();
                switch (cmd) {
                    case "1": manager.addTask(sc); userManager.saveToFile(); break;
                    case "2": manager.showTasks(); break;
                    case "3": manager.deleteTask(sc); userManager.saveToFile(); break;
                    case "4": manager.editTask(sc); userManager.saveToFile(); break;
                    case "5": manager.sortTasks(sc); userManager.saveToFile();break;
                    case "6": manager.searchTasks(sc); break;
                    case "7":
                        userManager.saveToFile();
                        System.out.println("Выход из аккаунта...");
                        exitToAuth = true;
                        break;
                    case "8":
                        userManager.saveToFile();
                        System.out.println("Данные сохранены. Выход из приложения...");
                        System.exit(0);
                    default:
                        System.out.println("Неверный ввод. Попробуйте снова.");
                }
            }
        }
    }
}