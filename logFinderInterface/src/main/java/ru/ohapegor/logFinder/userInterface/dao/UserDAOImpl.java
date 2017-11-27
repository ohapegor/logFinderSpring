package ru.ohapegor.logFinder.userInterface.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Stateless
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<User> getAllUsers() {
        logger.info("Entering UserDAOImpl.getAllUsers()");
        entityManager.joinTransaction();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        List<User> userList = query.getResultList();
        userList.forEach(user -> {
            if (user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))) {
                user.setBanned(true);
            } else {
                user.setBanned(false);
            }
        });
        logger.info("Exiting UserDAOImpl.getAllUsers()");
        return userList;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Entering UserDAOImpl.saveUser()");
        entityManager.joinTransaction();
        entityManager.persist(user);
        logger.info("Exiting UserDAOImpl.saveUser()");
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Entering UserDAOImpl.updateUser()");
        entityManager.joinTransaction();
        entityManager.merge(user);
        logger.info("Exiting UserDAOImpl.updateUser()");
    }

    @Override
    @Transactional
    public User getUserByName(String userName) {
        logger.info("Entering UserDAOImpl.getUserByName()");
        entityManager.joinTransaction();
        User user = entityManager.find(User.class, userName);
        if (user != null && user.getGroups().stream().anyMatch(group -> group.getGroupName().equalsIgnoreCase("BannedUsers"))) {
            user.setBanned(true);
        }
        logger.info("Exiting UserDAOImpl.saveUser()");
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        logger.info("Entering UserDAOImpl.deleteUser()");
        entityManager.joinTransaction();
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        logger.info("Exiting UserDAOImpl.deleteUser()");
    }

    @PreDestroy
    private void cleanup() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
