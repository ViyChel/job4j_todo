package ru.job4j.todo.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HbmRole;
import ru.job4j.todo.persistence.HbmTask;
import ru.job4j.todo.persistence.HbmUser;
import ru.job4j.todo.persistence.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class TaskServlet.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
@Slf4j
@WebServlet("/task.do")
public class TaskServlet extends HttpServlet {
    private static final Store<Task> TASK_STORE = HbmTask.getStore();
    private static final Store<User> USER_STORE = HbmUser.getStore();
    private static final Store<Role> ROLE_STORE = HbmRole.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        List<Task> tasks = TASK_STORE.findAll();
        JsonArray jsonArray = new JsonArray();
        PrintWriter writer = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Role roleUser = ROLE_STORE.findById(user.getRole());
            if ("admin".equals(roleUser.getName())) {
                fillJsonArray(jsonArray, user, tasks, roleUser);
            } else {
                List<Task> userTasks = tasks.stream()
                        .filter(el -> el.getUserId().equals(user.getId()))
                        .collect(Collectors.toList());
                fillJsonArray(jsonArray, user, userTasks, roleUser);
            }
        }
        writer.println(jsonArray);
    }

    private void fillJsonArray(JsonArray jsonArray, User user, List<Task> userTasks, Role role) {
        userTasks.forEach(task -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", task.getId());
            jsonObject.addProperty("description", task.getDescription());
            jsonObject.addProperty("created", dateFormat(task.getCreated()));
            jsonObject.addProperty("done", task.isDone());
            if ("admin".equals(role.getName())) {
                jsonObject.addProperty("user", USER_STORE.findById(task.getUserId()).getName());
            } else {
                jsonObject.addProperty("user", user.getName());
            }
            jsonArray.add(jsonObject);
        });
    }

    private String dateFormat(Timestamp timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = timestamp.toLocalDateTime();
        return localDate.format(formatter);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            while (reader.ready()) {
                sb.append(reader.readLine());
            }
        }
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            if (jsonObject.get("description") != null) {
                User user = ((User) req.getSession().getAttribute("user"));
                if (user == null) {
                    resp.getWriter().println("auth.do");
                } else {
                    String description = jsonObject.get("description").toString();
                    TASK_STORE.add(new Task(description, user.getId()));
                }
            } else {
                int id = Integer.parseInt(jsonObject.get("id").toString());
                boolean status = Boolean.parseBoolean(jsonObject.get("status").toString());
                Task task = TASK_STORE.findById(id);
                task.setDone(status);
                TASK_STORE.replace(id, task);
            }
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
    }
}
