package ru.job4j.todo.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.HbmCategory;
import ru.job4j.todo.persistence.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Class CategoryServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 10.01.2021
 */
@Slf4j
@WebServlet("/categories.do")
public class CategoryServlet extends HttpServlet {
    private static final Store<Category> CATEGORY_STORE = HbmCategory.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        List<Category> categories = CATEGORY_STORE.findAll();
        JsonArray jsonArray = new JsonArray();
        categories.forEach(category -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", category.getId());
            jsonObject.addProperty("name", category.getName());
            jsonArray.add(jsonObject);
        });
        resp.getWriter().println(jsonArray);
    }
}
