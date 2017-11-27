package ru.ohapegor.logFinder.userInterface.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GroupDAOImpl implements GroupDAO {

    private static final Logger logger = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
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
