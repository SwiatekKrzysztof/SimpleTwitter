package controllers.servlets;

import dao.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "loginServlet", urlPatterns = {"", "/login"})
public class LoginServlet extends HttpServlet {
    private static final int SECONDS_IN_DAY = 60 * 60 * 24;
    private static final String CHECKBOX_SELECTED = "on";
    private final String PASSWORD = "password";
    private final String LOGIN = "login";
    private final String REMEMBER = "remember";
    private final String LOGIN_COOKIE = "twitter_login";
    private final String PASSWORD_COOKIE = "twitter_password";

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        super.init();
    }

    /**
     * If user send request cookies will be checked
     * if any of logging depended are stored in browser
     * If those cookies exist, their MaxAge will be extended. Login and password
     * will be attached to request as a attribute and doPost() method will be called.
     * Otherwise login.jsp will be displayed
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = null;
        String password = null;
        if (null != req.getCookies()) {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals(LOGIN_COOKIE)) {
                    login = cookie.getValue();
                    cookie.setMaxAge(SECONDS_IN_DAY);
                    resp.addCookie(cookie);
                } else if (cookie.getName().equals(PASSWORD_COOKIE)) {
                    password = cookie.getValue();
                    cookie.setMaxAge(SECONDS_IN_DAY);
                    resp.addCookie(cookie);
                }
            }
        }
        if (null != login && null != password) {
            req.setAttribute(LOGIN, login);
            req.setAttribute(PASSWORD, password);
            doPost(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    /**
     * First, method tries to get login, password from parameters (parameters are sent by form),
     * if parameters are null it means that  doPost method was called by doGet() because doGet() method
     * is putting attributes instead of parameters.
     * In next step attribute are checked and login and password are set to attributes values.
     * Login and password are verified by dao isUserValid() method. If it is valid, login is saved
     * in session.
     * Next if remember checkbox is checked, cookies for login and password are created and attached to response
     *
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN);
        String password = req.getParameter(PASSWORD);
        String remember = req.getParameter(REMEMBER);

        PrintWriter writer = resp.getWriter();
        if (null == login || null == password) {
            login = (String) req.getAttribute(LOGIN);
            password = (String) req.getAttribute(PASSWORD);
            //req.getRequestDispatcher("/login.jsp").include(req, resp);
        }
        if (userDAO.isUserValid(login, password)) {
            req.getSession().setAttribute(LOGIN, login);
            if (null != remember && remember.equals(CHECKBOX_SELECTED)) {
                Cookie loginCookie = new Cookie(LOGIN_COOKIE, login);
                Cookie passwordCookie = new Cookie(PASSWORD_COOKIE, password);
                loginCookie.setMaxAge(SECONDS_IN_DAY);
                passwordCookie.setMaxAge(SECONDS_IN_DAY);
                resp.addCookie(loginCookie);
                resp.addCookie(passwordCookie);
            }
            req.getRequestDispatcher("users").forward(req, resp);
        } else {
          req.setAttribute("hasError","true");
          req.setAttribute("error","Login or password incorrect!");
          req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }
    }

}
