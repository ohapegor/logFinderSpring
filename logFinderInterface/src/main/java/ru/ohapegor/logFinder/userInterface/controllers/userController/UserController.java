package ru.ohapegor.logFinder.userInterface.controllers.userController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.dao.GroupDAO;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessageSender;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserController {

    private static final Logger logger = LogManager.getLogger();

    private List<User> userList;

    @EJB
    private JmsMessageSender messageSender;

    @EJB
    private UserDAO userDAO;

    @EJB
    private GroupDAO groupDAO;

    @PostConstruct
    private void init() {
        logger.info("Entering UserController.init()");
        userList = userDAO.getAllUsers();
        logger.info("Exiting UserController.init()");
    }

    public List<User> getUserList() {
        return userList;
    }


    public void deleteUser(User user) {
        logger.info("Entering UserController.deleteUser()");
        userDAO.deleteUser(user);
        loadUsers();
        logger.info("Exiting UserController.deleteUser()");
    }

    public void loadUsers() {
        logger.info("Entering UserController.loadUsers()");
        userList = userDAO.getAllUsers();
        logger.info("Exiting UserController.loadUsers()");
    }

    public void ban(User user) {
        logger.info("Entering UserController.ban()");
        user.getGroups().add(groupDAO.getGroupByName("BannedUsers"));
        userDAO.updateUser(user);
        loadUsers();
        logger.info("Exiting UserController.ban()");
    }

    public void unBan(User user) {
        logger.info("Entering UserController.ban()");
        user.getGroups().remove(groupDAO.getGroupByName("BannedUsers"));
        /*user.getGroups().forEach(group -> {
            if (group.getGroupName().equalsIgnoreCase("BannedUsers")) {
                user.getGroups().remove(group);
            }
        });*/
        user.setBanned(false);
        userDAO.updateUser(user);
        loadUsers();
        logger.info("Exiting UserController.ban()");
    }

    public void sendMessage(User user)  {
        logger.info("Entering UserController.sendJmsMessage(); message = " + user.getMessageToSend()+"; address = "+user.getEmail());
        messageSender.sendJmsMessage(user.getMessageToSend(),user.getEmail());
        user.setMessageToSend(null);
        logger.info("Exiting UserController.sendJmsMessage()");
    }

    public void reload() {
        logger.info("Entering UserController.reload()");
        userList = userDAO.getAllUsers();
        logger.info("Exiting UserController.reload()");
    }
}
