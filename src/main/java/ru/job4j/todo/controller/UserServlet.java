package ru.job4j.todo.controller;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.todo.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Class UserServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 28.12.2020
 */
@Slf4j
@WebServlet("/user.do")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String name = "";
        if (!((req.getSession().getAttribute("user")) == null)) {
            name = ((User) req.getSession().getAttribute("user")).getName();
        }
        PrintWriter pw = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        pw.print(name);
        pw.flush();
    }
}
