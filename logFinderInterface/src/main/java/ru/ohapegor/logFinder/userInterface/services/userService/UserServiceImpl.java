package ru.ohapegor.logFinder.userInterface.services.userService;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override

    public User getUserByName(String userName) {
        logger.info("Entering UserServiceImpl.getUserByName()");
        User user = userDAO.getUserByName(userName);
        if (user != null && user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))){
            user.setBanned(true);
        }
        logger.info("Exiting UserServiceImpl.getUserByName()");
        return user;
    }

    @Override
    public void saveUser(User user) {
        logger.info("Entering UserServiceImpl.getUserByName()");
        userDAO.saveUser(user);
        logger.info("Exiting UserServiceImpl.getUserByName()");
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Entering UserServiceImpl.getUserByName()");
        List <User> userList = userDAO.getAllUsers();
        userList.forEach(user -> {
            if (user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))) {
                user.setBanned(true);
            } else {
                user.setBanned(false);
            }
        });
        logger.info("Exiting UserServiceImpl.getUserByName()");
        return userList;
    }

    @Override
    public void deleteUser(User user) {
        logger.info("Entering UserServiceImpl.getUserByName()");
        userDAO.deleteUser(user);
        logger.info("Exiting UserServiceImpl.getUserByName()");
    }

    @Override
    public void updateUser(User user) {
        logger.info("Entering UserServiceImpl.getUserByName()");
        userDAO.updateUser(user);
        logger.info("Exiting UserServiceImpl.getUserByName()");
    }

}