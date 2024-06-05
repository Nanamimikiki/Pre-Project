package jdbc.dao;

import jdbc.service.UserService;
import jdbc.service.UserServiceImpl;

public class Main {
    private final static UserService userService = new UserServiceImpl();

    public static void main(String[] args) {
        userService.createUsersTable();
        userService.saveUser("Джо", "Байден", (byte) 78);
        userService.saveUser("Дональд", "Трамп", (byte) 74);
        userService.saveUser("Барак", "Обама", (byte) 59);
        userService.saveUser("Джордж", "Буш", (byte) 74);

        userService.removeUserById(2);

        userService.getAllUsers();

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
