package ru.ohapegor.logFinder.userInterface.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;


@Stateless
public class UserDAOImpl1 implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    //@PersistenceContext
    private EntityManager entityManager;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    private void init() {
        logger.info("Entering UserDAOImpl1.init()");
        entityManager = entityManagerFactory.createEntityManager();
        logger.info("Exiting UserDAOImpl1.init()");
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        logger.info("Entering UserDAOImpl1.getAllUsers()");
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        List<User> userList = query.getResultList();
        userList.forEach(user -> {
            if (user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))) {
                user.setBanned(true);
            } else {
                user.setBanned(false);
            }
        });
        logger.info("Exiting UserDAOImpl1.getAllUsers()");
        return userList;
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user){
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public User getUserByName(String userName) {
        logger.info("Entering UserDAOImpl1.getUserByName()");
        User user = entityManager.find(User.class, userName);
        if (user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))){
            user.setBanned(true);
        }
        logger.info("Exiting UserDAOImpl1.saveUser()");
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        logger.info("Entering UserDAOImpl1.deleteUser()");
        entityManager.remove(user);
        logger.info("Exiting UserDAOImpl1.deleteUser()");
    }

}
