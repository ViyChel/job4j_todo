package ru.job4j.todo.controller;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HbmUser;
import ru.job4j.todo.persistence.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class AuthServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.12.2020
 */
@Slf4j
@WebServlet("/auth.do")
public class AuthServlet extends HttpServlet {
    private static final Store<User> USER_STORE = HbmUser.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        if ("true".equals(req.getParameter("exit"))) {
            req.getSession().setAttribute("user", null);
            resp.sendRedirect(req.getContextPath());
        } else {
            req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        if (!"".equals(email) && !"".equals(password)) {
            User user = ((HbmUser) USER_STORE).findByEmail(email);
            if (user.getId() != null && user.getPassword().equals(password)) {
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath());
            } else {
                authFail(req, resp);
            }
        } else {
            authFail(req, resp);
        }
    }

    private void authFail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "Не верный email или пароль");
        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }
}
