package ru.job4j.todo.controller;

import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HbmRole;
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
 * Class RegServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 24.11.2020
 */
@WebServlet("/reg.do")
public class RegServlet extends HttpServlet {
    private static final Store<User> USER_STORE = HbmUser.getStore();
    private static final Store<Role> ROLE_STORE = HbmRole.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("/auth/reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            User user = ((HbmUser) USER_STORE).findByEmail(email);
            if (user.getId() != null) {
                req.setAttribute("error", "Пользователь с таким email уже существует!");
                doGet(req, resp);
            } else {
                Role role = ROLE_STORE.findByName("user").get(0);
                user = User.of(name, email, password, role);
                USER_STORE.add(user);
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath());
            }
        } else {
            req.setAttribute("error", "Заполните все поля!");
            doGet(req, resp);
        }
    }
}
