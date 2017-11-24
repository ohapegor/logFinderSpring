package ru.ohapegor.logFinder.userInterface.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;


import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.util.List;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

//@Stateless(name = "userDao")
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    private SessionFactory factory;

    @PostConstruct
    public void init(){
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.setProperty("hibernate.connection.datasource", "jdbc/usersDS");
            factory = configuration.buildSessionFactory();
        }catch (Exception e){
            logger.error("Exception in UserDAOImpl while building session factory : " + getStackTrace(e));
            throw new ExceptionInInitializerError("Failed building session factory.");
        }
    }


    @Override
    public User getUserByName(String username) {
        logger.info("Entering UserDAOImpl.getUserByName()");
        Transaction tx = null;
        User user = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            user = session.get(User.class, username);
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception in UserDAOImpl.getUserByName : " + getStackTrace(e));
            if (tx!=null) tx.rollback();
        }
        logger.info("Exiting UserDAOImpl.getUserByName()");
        return user;

    }

    @Override
    public void deleteUser(User user) {
        logger.info("Entering UserDAOImpl.deleteUser()");
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception in UserDAOImpl.deleteUser : " + getStackTrace(e));
            if (tx!=null) tx.rollback();
        }
        logger.info("Exiting UserDAOImpl.getUserByName()");
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void saveUser(User user) {
        logger.info("Entering UserDAOImpl.getUserByName()");
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception in UserDAOImpl.saveUser : " + getStackTrace(e));
            if (tx!=null) tx.rollback();
        }
        logger.info("Exiting UserDAOImpl.getUserByName()");
    }
}
