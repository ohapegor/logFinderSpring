package ru.ohapegor.logFinder.userInterface.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Entering UserDAOImpl.updateUser()");
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
        logger.info("UserDAO.updateUser()");
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByName(String username) {
        logger.info("Entering UserDAOImpl.getUserByName()");
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, username);
        logger.info("UserDAO.getUserByName() finished, User - " + username + " successfully loaded from DB");
        return user;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Entering UserDAOImpl.saveUser()");
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        logger.info("UserDAO.saveUser() finished, User - " + user.getUserName() + " successfully saved in DB");
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Entering UserDAOImpl.getAllUsers()");
        Session session = sessionFactory.getCurrentSession();
        List<User> users = session.createQuery("from User").list();
        logger.info("Exiting UserDAOImpl.getAllUsers()");
        return users;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        logger.info("Entering UserDAOImpl.deleteUser()");
        Session session = sessionFactory.getCurrentSession();
        session.delete(user);
        logger.info("Exiting UserDAOImpl.deleteUser()");

    }


}
