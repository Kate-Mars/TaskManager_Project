import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class UserManager {
    private List<User> users;
    private final String FILE_NAME = "users.json";
    private final Gson gson;

    public UserManager() {
        this.users = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public User login(Scanner sc) {
        System.out.println("   АВТОРИЗАЦИЯ   ");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.print("Выберите действие: ");

        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                return loginUser(sc);
            case "2":
                return registerUser(sc);
            default:
                System.out.println("Неверный выбор");
                return null;
        }
    }

    private User loginUser(Scanner sc) {
        System.out.print("Введите имя пользователя: ");
        String username = sc.nextLine();
        System.out.print("Введите пароль: ");
        String password = sc.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Успешный вход! Добро пожаловать, " + username);
                return user;
            }
        }

        System.out.println("Неверное имя пользователя или пароль");
        return null;
    }

    private User registerUser(Scanner sc) {
        System.out.print("Введите новое имя пользователя: ");
        String username = sc.nextLine();

        // Проверка на уникальность имени
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Пользователь с таким именем уже существует");
                return null;
            }
        }

        System.out.print("Введите пароль: ");
        String password = sc.nextLine();

        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("Пользователь " + username + " успешно зарегистрирован!");
        return newUser;
    }

    public void loadFromFile() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> loadedUsers = gson.fromJson(reader, userListType);
            if (loadedUsers != null) {
                this.users = loadedUsers;
                System.out.println("Данные успешно загружены из " + FILE_NAME);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден. Будет создан новый при сохранении.");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
            System.out.println("Данные успешно сохранены в " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        return users;
    }
}