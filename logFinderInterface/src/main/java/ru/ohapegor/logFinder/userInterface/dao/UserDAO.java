package ru.ohapegor.logFinder.userInterface.dao;

import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;


public interface UserDAO {

    void updateUser(User user);

    User getUserByName(String username);

    void saveUser(User user);

    List<User> getAllUsers();

    void deleteUser(User user);


}
