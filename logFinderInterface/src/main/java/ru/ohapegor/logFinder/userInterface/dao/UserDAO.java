package ru.ohapegor.logFinder.userInterface.dao;

import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;


public interface UserDAO {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserByName(String userName);

    void deleteUser(User user);

    void updateUser(User user);

}


