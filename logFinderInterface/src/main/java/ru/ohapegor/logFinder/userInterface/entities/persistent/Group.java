package ru.ohapegor.logFinder.userInterface.entities.persistent;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group{

    private String groupName;
    private String description;
    private Set<User> userList;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group() {
    }

    public Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    @Id
    @Column(name = "G_NAME")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "G_DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "groupmembers",
            joinColumns = @JoinColumn(name = "G_NAME"),
            inverseJoinColumns = @JoinColumn(name = "G_MEMBER"))*/
    @ManyToMany(mappedBy = "groups")
    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return groupName != null && groupName.equals(group.groupName);
    }

    @Override
    public int hashCode() {
        return groupName != null ? groupName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return groupName;
    }

}