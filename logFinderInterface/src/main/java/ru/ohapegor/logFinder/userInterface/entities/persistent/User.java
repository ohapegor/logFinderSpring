package ru.ohapegor.logFinder.userInterface.entities.persistent;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "users")
public class User{

    private String userName;
    private String password;
    private Calendar registrationTime;
    private String description;
    private Set<Group> groups;
    private String email;
    private String messageToSend;
    private boolean Banned;

    @Transient
    public String getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
    }

    @Transient
    public boolean isBanned() {
        return Banned;
    }

    public void setBanned(boolean banned) {
        Banned = banned;
    }

    @Id
    @Column(name = "U_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "U_EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "U_PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_time")
    public Calendar getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Calendar registrationTime) {
        this.registrationTime = registrationTime;
    }

    @Column(name = "U_DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "groupmembers",
            joinColumns = @JoinColumn(name = "G_MEMBER"),
            inverseJoinColumns = @JoinColumn(name = "G_NAME"))
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public User() {
    }

    public User(String userName, String password, String description, Set<Group> groups) {
        this.userName = userName;
        this.password = password;
        this.description = description;
        this.groups = groups;
    }
}
