package ru.ohapegor.logFinder.userInterface.dao.hibernate;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.dao.GroupDAO;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Repository("groupDaoJpa")
public class GroupDaoHibernate implements GroupDAO {

    private static final Logger logger = LogManager.getLogger();

    private SessionFactory sessionFactory;

    //@Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroupByName(String groupName) {
        logger.info("Entering UserDaoHibernate.getGroupByName()");
        Group group = (Group) sessionFactory.getCurrentSession().get(Group.class,groupName);
        logger.info("Exiting UserDaoHibernate.getGroupByName()");
        return group;
    }

}
