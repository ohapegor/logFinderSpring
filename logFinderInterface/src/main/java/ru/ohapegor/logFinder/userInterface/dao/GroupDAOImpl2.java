package ru.ohapegor.logFinder.userInterface.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class GroupDAOImpl2 implements GroupDAO {

    private static final Logger logger = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    @Transactional(readOnly = true)
    public Group getGroupByName(String groupName) {
        logger.info("Entering UserDAOImpl.getGroupByName()");
        Group group = entityManager.find(Group.class,groupName);
        logger.info("Exiting UserDAOImpl.getGroupByName()");
        return group;
    }

    @PreDestroy
    private void cleanup() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
