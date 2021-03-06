package ru.ohapegor.logFinder.userInterface.entities.persistent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private String userName;
    private String password;
    private Calendar registrationTime;
    private String description;
    private String email;
    private Set<Group> groups;
    private String messageToSend;

    @Transient
    public String getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
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

    @Temporal(TemporalType.TIMESTAMP)
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


    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    public User(String userName, String password, String email, String description, Set<Group> groups) {
        this.userName = userName;
        this.password = password;
        this.description = description;
        this.email = email;
        this.groups = groups;
    }

    public User(String userName, String password, String email, String description) {
        this.userName = userName;
        this.password = password;
        this.description = description;
        this.email = email;
    }
}
