package ru.ohapegor.logFinder.userInterface.controllers.userControl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ohapegor.logFinder.userInterface.dao.GroupDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessage;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessageSender;
import ru.ohapegor.logFinder.userInterface.services.userService.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component("userBean")
@Scope("session")
public class UserController {

    private static final Logger logger = LogManager.getLogger();

    private List<User> userList;

    private UserService userService;

    private JmsMessageSender messageSender;

    @Autowired
    public void setMessageSender(JmsMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }

    @PostConstruct
    private void init() {
        logger.info("Entering UserController.init()");
        userList = userService.getAllUsers();
        logger.info("Exiting UserController.init()");
    }

    public void deleteUser(User user) {
        logger.info("Entering UserController.deleteUser()");
        userService.deleteUser(user);
        userList = userService.getAllUsers();
        logger.info("Exiting UserController.deleteUser()");
    }

    public void ban(User user) {
        logger.info("Entering UserController.ban()");
        userService.ban(user);
        userList = userService.getAllUsers();
        logger.info("Exiting UserController.ban()");
    }

    public void unBan(User user) {
        logger.info("Entering UserController.unBan()");
        userService.unBan(user);
        userList = userService.getAllUsers();
        logger.info("Exiting UserController.unBan()");
    }

    public void refresh() {
        logger.info("Entering UserController.refresh()");
        userList = userService.getAllUsers();
        logger.info("Exiting UserController.refresh()");
    }

    public void sendMessageTo(User user)  {
        logger.info("Entering UserController.sendMessageTo(); message = " + user.getMessageToSend()+"; address = "+user.getEmail());
        messageSender.sendJmsMessage(new JmsMessage(user.getMessageToSend(),user.getEmail()));
        //clean message field
        user.setMessageToSend(null);
        logger.info("Exiting UserController.sendMessageTo()");
    }

    public boolean isBanned(User user){
        return user.getGroups().stream().anyMatch(group -> group.getGroupName().equals("BannedUsers"));
    }

    public boolean isAdmin(User user){
        return user.getGroups().stream().anyMatch(group -> group.getGroupName().equals("SuperAdmins"));
    }
}