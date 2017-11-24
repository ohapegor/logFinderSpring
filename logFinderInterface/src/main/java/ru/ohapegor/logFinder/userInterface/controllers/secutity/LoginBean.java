package ru.ohapegor.logFinder.userInterface.controllers.secutity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.dao.UserDAO;
import ru.ohapegor.logFinder.userInterface.dao.UserDAOImpl;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

@Named
@Stateless
@SessionScoped
public class LoginBean {

    private static final Logger logger = LogManager.getLogger();

    private String userName;
    private String password;
    private String confirmPassword;
    private String message;

    @EJB
    private UserDAO userDAO;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(true);

        try {
            request.logout();
            request.login(this.userName, this.password);
            session.setAttribute("username", userName);
            session.setAttribute("password", password);
            logger.fatal("User successfully logged in, userName : " + userName);
            userName = null;
            password = null;
        } catch (ServletException e) {
            logger.info("Exception in LoginBean.login() : " + getStackTrace(e));
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Login failed."));
            return "/pages/errorPage";
        }
        return "/pages/home?faces-redirect=true";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        try {
            if (session.getAttribute("username") != null) userName = String.valueOf(session.getAttribute("username"));
            request.logout();
            request.getSession().invalidate();
            logger.fatal("User successfully logged out, userName : " + userName);
            userName = null;
            password = null;
        } catch (Exception e) {
            session = request.getSession(true);
            session.setAttribute("errorMessage", "logoutFailed, Exception : " + e);
            context.addMessage(null, new FacesMessage("Logout failed."));
            logger.info("Exception in LoginBean.logout() : " + getStackTrace(e));
            return "/pages/errorPage";
        }

        return "/pages/login?faces-redirect=true";
    }

    public void exit() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session != null) session.invalidate();
        //return "about:blank";
    }

    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (userName == null || userName.equals("")) {
            context.addMessage("", new FacesMessage(">>Empty User Name<<"));
            return "/pages/registration";
        }

        if (userName.length() < 5 || userName.length() > 15) {
            context.addMessage("", new FacesMessage(">>Username length must be between 5 and 15 characters<<"));
            return "/pages/registration";
        }

        if (password == null || password.equals("")) {
            context.addMessage("", new FacesMessage(">>Empty password<<"));
            return "/pages/registration";
        }

        if (password.length() < 10 || userName.length() > 25) {
            context.addMessage("", new FacesMessage(">>Password length must be between 10 and 25 characters<<"));
            return "/pages/registration";
        }


        if (!password.equals(confirmPassword)) {
            context.addMessage("", new FacesMessage(">>Passwords don't match<<"));
            return "/pages/registration";
        }

        try {
            User user = userDAO.getUserByName(userName);
            if (user != null) {
                context.addMessage("", new FacesMessage(">>User already exists!<<"));
                return "/pages/registration";
            }

            String description = "Registration time: " + new Date().toString();
            user = new User(userName, password, description, Collections.singleton(Group.NEW_USERS));
            userDAO.saveUser(user);

            context.addMessage(null, new FacesMessage("User : " + userName + " successfully registered!"));
            logger.fatal("Developer's log: New user registered, userName : " + userName);
        } catch (Exception e) {
            logger.error("Exception in LoginBean.register(), e : " + getStackTrace(e));
            context.addMessage(null, new FacesMessage("Exception in LoginBean.register(), e : " + getStackTrace(e)));
        }
        return "/pages/registration";

    }
}
