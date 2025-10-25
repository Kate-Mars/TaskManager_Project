package app.util;

import app.model.User;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static List<User> cachedUsers = new ArrayList<>();

    public static void initialize() {
        System.out.println("FileStorage initialized");
    }

    public static void saveAll() {
        System.out.println("FileStorage saved all users");
    }

    public static List<User> getAllUsers() {
        return cachedUsers;
    }

    public static void setAllUsers(List<User> users) {
        cachedUsers = users;
    }
}
