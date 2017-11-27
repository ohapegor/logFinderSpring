package ru.ohapegor.logFinder.userInterface.controllers.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ohapegor.logFinder.userInterface.entities.persistent.Group;
import ru.ohapegor.logFinder.userInterface.entities.persistent.User;
import ru.ohapegor.logFinder.userInterface.services.userService.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Date;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

@Component("loginBean")
@Scope("session")
public class LoginBean {

    //private final static Logger logger = Logger.getLogger(UserInterfaceBean.class.getName());
    private static final Logger logger = LogManager.getLogger(LoginBean.class.getSimpleName());

    private UserService userService;

    @Autowired
    @Qualifier("userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private String userName;
    private String password;
    private String confirmPassword;
    private String userEmail;
    private String message;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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
            logger.fatal("User successfully logged in, userName : "+userName);
            userName = null;
            password = null;
        } catch (ServletException e) {
            logger.info("Exception in LoginBean.login() : " + getStackTrace(e));
            context.addMessage(null, new FacesMessage("Login failed. "+e));
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
            logger.fatal("User successfully logged out, userName : "+userName);
            userName = null;
            password = null;
        } catch (Exception e) {
            session = request.getSession(true);
            session.setAttribute("errorMessage", "logoutFailed, Exception : " + e);
            context.addMessage(null, new FacesMessage("Logout failed."));
            logger.info("Exception in LoginBean.logout() : " + e);
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
        logger.info("Entering in LoginBean.register()");
        FacesContext context = FacesContext.getCurrentInstance();

        if (userName == null || userName.equals("")){
            context.addMessage("", new FacesMessage(">>Empty User Name<<"));
            return "/pages/registration";
        }

        if (userName.length() < 5 || userName.length() > 15){
            context.addMessage("", new FacesMessage(">>Username length must be between 5 and 15 characters<<"));
            return "/pages/registration";
        }

        if (password == null || password.equals("")){
            context.addMessage("", new FacesMessage(">>Empty password<<"));
            return "/pages/registration";
        }

        if (password.length() < 10 || userName.length() > 25){
            context.addMessage("", new FacesMessage(">>Password length must be between 10 and 25 characters<<"));
            return "/pages/registration";
        }


        if (!password.equals(confirmPassword)) {
            context.addMessage("", new FacesMessage(">>Passwords don't match<<"));
            return "/pages/registration";
        }

        try{
            User user = userService.getUserByName(userName);
            if (user != null) {
                context.addMessage("", new FacesMessage(">>User already exists!<<"));
                return "/pages/registration";
            }

            String description = "Registration time: " + new Date().toString();
            user = new User(userName, password, description,  Collections.singleton(Group.NEW_USERS), userEmail);
            userService.saveUser(user);

            context.addMessage(null, new FacesMessage("User : " + userName + " successfully registered!"));
            logger.fatal("Developer's log: New user registered, userName : "+userName);
        }catch (Exception e){
            logger.error("Exception in LoginBean.register(), e : "+getStackTrace(e));
            context.addMessage(null, new FacesMessage("Exception in LoginBean.register(), e : "+getStackTrace(e)));
        }
        return "/pages/registration";

    }
}
