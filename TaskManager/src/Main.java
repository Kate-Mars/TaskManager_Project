import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("    To-Do List    ");
        TaskManager manager = new TaskManager();
        while (true) {
            System.out.println("1. Добавить задачу");
            System.out.println("2. Показать задачи");
            System.out.println("3. Удалить задачу");
            System.out.println("4. Изменить задачу");
            System.out.println("5. Сортировать задачи");
            System.out.println("6. Поиск задач");
            System.out.println("7. Выход");
            String cmd = sc.nextLine();
            switch (cmd) {
                case "1": manager.addTask(sc); break;
                case "2": manager.showTasks(); break;
                case "3": manager.deleteTask(sc); break;
                case "4": break;
                case "5": break;
                case "6": break;
                case "7": return;
                default: System.out.println("Неверный ввод");
            }
        }
    }
}
