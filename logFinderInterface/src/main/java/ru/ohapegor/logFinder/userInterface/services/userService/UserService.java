package ru.ohapegor.logFinder.userInterface.services.userService;


import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;

public interface UserService {

    User getUserByName(String userName);

    void saveUser(User user);

    List<User> getAllUsers();

    void deleteUser(User user);

    void updateUser(User user);
}
