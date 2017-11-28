package ru.ohapegor.logFinder.userInterface.dao.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;

import java.util.List;

//using hibernate
@Repository("userDaoHibernate")
public class UserDaoHibernate implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    private SessionFactory sessionFactory;

    //@Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Entering UserDaoHibernate.updateUser()");
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
        logger.info("UserDAO.updateUser()");
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByName(String username) {
        logger.info("Entering UserDaoHibernate.getUserByName()");
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.get(User.class, username);
        logger.info("UserDAO.getUserByName() finished, User - " + username + " successfully loaded from DB");
        return user;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Entering UserDaoHibernate.saveUser()");
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        logger.info("UserDAO.saveUser() finished, User - " + user.getUserName() + " successfully saved in DB");
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Entering UserDaoHibernate.getAllUsers()");
        Session session = sessionFactory.getCurrentSession();
        List<User> users = session.createQuery("from User").list();
        logger.info("Exiting UserDaoHibernate.getAllUsers()");
        return users;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        logger.info("Entering UserDaoHibernate.deleteUser()");
        Session session = sessionFactory.getCurrentSession();
        session.delete(user);
        logger.info("Exiting UserDaoHibernate.deleteUser()");

    }


}
