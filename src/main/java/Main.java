import jdbc.model.User;
import jdbc.service.UserService;
import jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    private final static UserService userService = new UserServiceImpl();

    public static void main(String[] args) {

        userService.createUsersTable();

        userService.saveUser("Joe", "Biden", (byte) 78);
        userService.saveUser("Donald", "Trump", (byte) 74);
        userService.saveUser("Барак", "Обама", (byte) 59);
        userService.saveUser("Джордж", "Буш", (byte) 74);

        userService.removeUserById(1);

        List<User> usersTable = userService.getAllUsers();
        for (User us : usersTable) {
            System.out.println(us);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
