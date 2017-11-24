package ru.ohapegor.logFinder.userInterface.controllers.secutity;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter("/pages/secured/*")
public class LoginFilter implements Filter {
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
            session = httpServletRequest.getSession(true);

            if (session == null) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/login.xhtml");
            }else {
                userName = String.valueOf(session.getAttribute("username"));
                password = String.valueOf(session.getAttribute("password"));
            }

            if (userName == null || userName.equals("null") || password == null || password.equals("null"))
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/login.xhtml");
            //httpServletRequest.getRequestDispatcher("login.xhtml").forward(request,response);

            String user = httpServletRequest.getRemoteUser();

            if (user == null) httpServletRequest.login(userName, password);

            switch (path) {
                case "/pages/secured/UserInterface.xhtml":
                    if (!httpServletRequest.isUserInRole("User")) {
                        session.setAttribute("errorMessage", "Access denied");
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/errorPage.xhtml");
                    }
                    break;
                case "/pages/secured/adminPage.xhtml":
                    if (!httpServletRequest.isUserInRole("SuperAdmin")) {
                        session.setAttribute("errorMessage", "Access denied");
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/errorPage.xhtml");
                        // httpServletResponse.sendError(403,"error : Access denied by filter admin");
                    }
                    break;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            httpServletResponse.sendError(500, "Exception in filter : " + e);
        }

    }

    @Override
    public void destroy() {

    }
}
