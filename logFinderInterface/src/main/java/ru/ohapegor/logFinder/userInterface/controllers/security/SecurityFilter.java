package ru.ohapegor.logFinder.userInterface.controllers.security;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@WebFilter("/pages/secured/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = null;
        HttpServletResponse httpServletResponse = null;
        HttpSession session = null;
        String userName = null;
        String password = null;
        try {
            httpServletRequest = (HttpServletRequest) request;
            httpServletResponse = (HttpServletResponse) response;
            String path = httpServletRequest.getServletPath();
            session = httpServletRequest.getSession(false);

            //create session if not exist and redirect to login page
            if (session == null) {
                session = httpServletRequest.getSession(true);
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/login.xhtml");
            } else {
                userName = String.valueOf(session.getAttribute("userName"));
                password = String.valueOf(session.getAttribute("password"));
            }

            //if no user and password stored in session -> redirect to login page
            if (userName == null || userName.equals("null") || password == null || password.equals("null")) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/login.xhtml");
            }

            //login in weblogic
            String user = httpServletRequest.getRemoteUser();
            if (user == null) {
                httpServletRequest.login(userName, password);
            }

            //check roles
            if (path.startsWith("/pages/secured/") && httpServletRequest.isUserInRole("Banned")){
                session.setAttribute("errorMessage", "You were banned!");
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/errorPage.xhtml");
            }

                switch (path) {
                    case "/pages/secured/UserInterface.xhtml":
                        //only users and admin allowed to search logs
                        if (!(httpServletRequest.isUserInRole("SuperAdmin") || httpServletRequest.isUserInRole("User"))) {
                            session.setAttribute("errorMessage", "Access denied");
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/errorPage.xhtml");
                        }
                        break;
                    //only admin allowed to view admin page
                    case "/pages/secured/adminPage.xhtml":
                        if (!httpServletRequest.isUserInRole("SuperAdmin")) {
                            session.setAttribute("errorMessage", "Access denied");
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/errorPage.xhtml");
                        }
                        break;
                }
            //continue request if role is correct
            chain.doFilter(request, response);
        } catch (Exception e) {
            httpServletResponse.sendError(500, "Exception in filter : " + getStackTrace(e));
        }

    }

    @Override
    public void destroy() {

    }
}
