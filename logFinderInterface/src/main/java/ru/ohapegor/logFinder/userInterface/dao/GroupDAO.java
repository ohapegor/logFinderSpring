package ru.ohapegor.logFinder.userInterface.dao;


import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;

public interface GroupDAO {

    Group getGroupByName(String groupName);
}
