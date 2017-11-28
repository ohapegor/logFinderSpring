package ru.ohapegor.logFinder.userInterface.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

//using jpa
@Repository("userDao2")
@Transactional(readOnly = true)
public class UserDAOImpl2 implements UserDAO{

    private static final Logger logger = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        logger.info("Entering UserDAOImpl.getAllUsers()");
        entityManager.joinTransaction();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        List<User> userList = query.getResultList();
        logger.info("Exiting UserDAOImpl.getAllUsers()");
        return userList;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Entering UserDAOImpl.saveUser()");
        entityManager.persist(user);
        logger.info("Exiting UserDAOImpl.saveUser()");
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Entering UserDAOImpl.updateUser()");
        entityManager.merge(user);
        logger.info("Exiting UserDAOImpl.updateUser()");
    }

    @Override
    public User getUserByName(String userName) {
        logger.info("Entering UserDAOImpl.getUserByName()");
        User user = entityManager.find(User.class, userName);
        logger.info("Exiting UserDAOImpl.saveUser()");
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        logger.info("Entering UserDAOImpl.deleteUser()");
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
