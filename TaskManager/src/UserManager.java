/**
 * Manages user authentication and registration.
 * Handles user login, registration, and persistence of user data to JSON file.
 *
 * Main features:
 * - New user registration with username uniqueness validation
 * - User login with credentials verification
 * - Automatic loading of user data from users.json on startup
 * - Automatic saving of user data to users.json on changes
 * - Secure password handling (Note: passwords stored in plain text)
 *
 * @version 1.0
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private List<User> users;
    private final String FILE_NAME = "users.json";
    private final Gson gson;

    public UserManager() {
        this.users = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public User login(Scanner sc) {
        while (true) {
            System.out.println("\n=== АВТОРИЗАЦИЯ ===");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("3. Выйти из приложения");
            System.out.print("Выберите действие: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    User user = loginUser(sc);
                    if (user != null) {
                        return user;
                    }
                    break;
                case "2":
                    user = registerUser(sc);
                    if (user != null) {
                        return user;
                    }
                    break;
                case "3":
                    System.out.println("Выход из приложения. До свидания!");
                    saveToFile();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private User loginUser(Scanner sc) {
        while (true) {
            System.out.println("\n--- Вход в систему ---");
            System.out.println("Введите '!' для возврата в главное меню");
            System.out.print("Введите имя пользователя: ");
            String username = sc.nextLine();

            if ("!".equals(username)) { return null; }

            User foundUser = null;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    foundUser = user;
                    break;
                }
            }

            if (foundUser == null) {
                System.out.println("Пользователь с таким именем не найден. Попробуйте снова.");
                continue;
            }

            return enterPassword(sc, foundUser);
        }
    }

    private User enterPassword(Scanner sc, User user) {
        while (true) {
            System.out.println("Введите '!' для возврата к выбору пользователя");
            System.out.print("Введите пароль для пользователя '" + user.getUsername() + "': ");
            String password = sc.nextLine();

            if ("!".equals(password)) { return null; }

            if (user.getPassword().equals(password)) {
                System.out.println("Успешный вход! Добро пожаловать, " + user.getUsername());
                return user;
            } else {
                System.out.println("Неверный пароль. Попробуйте снова.");
            }
        }
    }

    private User registerUser(Scanner sc) {
        System.out.println("\n--- Регистрация ---");
        System.out.println("Введите '!' для возврата в главное меню");

        while (true) {
            System.out.print("Введите новое имя пользователя: ");
            String username = sc.nextLine();

            if ("!".equals(username)) { return null; }

            if (username.isEmpty()) {
                System.out.println("Имя пользователя не может быть пустым.");
                continue;
            }

            boolean usernameExists = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    usernameExists = true;
                    break;
                }
            }

            if (usernameExists) {
                System.out.println("Пользователь с таким именем уже существует. Попробуйте другое имя.");
                continue;
            }

            System.out.print("Введите пароль: ");

            String password = sc.nextLine();
            if (password.isEmpty()) {
                System.out.println("Пароль не может быть пустым.");
                continue;
            }

            User newUser = new User(username, password);
            users.add(newUser);
            System.out.println("Пользователь " + username + " успешно зарегистрирован!");
            return newUser;
        }
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