package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;

import java.util.List;

/**
 * Class HbmTask.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
@Slf4j
public class HbmTask implements Store<Task> {
    private static final HbmTask STORE = new HbmTask();

    public static HbmTask getStore() {
        return STORE;
    }

    @Override
    public Task add(Task model) {
        this.tx(session -> session.save(model));
        return model;
    }


    public Task add(Task task, String[] ids) {
        this.tx(session -> {
            for (String id : ids) {
                Category category = session.find(Category.class, Integer.parseInt(id));
                task.addCategory(category);
            }
            session.persist(task);
            return true;
        });
        return task;
    }

    @Override
    public boolean replace(int id, Task model) {
        return this.tx(session -> {
            session.update(String.valueOf(id), model);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return this.tx(session -> {
            Task model = session.get(Task.class, id);
            session.remove(model);
            return true;
        });
    }

    @Override
    public List<Task> findAll() {
        return this.tx(session -> session.createQuery("select distinct c from Task c join fetch c.categories",
                Task.class).list());
    }

    @Override
    public List<Task> findByName(String name) {
        return this.tx(session -> session.createQuery("from Task where description = :description", Task.class)
                .setParameter("description", name).list()
        );
    }

    @Override
    public Task findById(int id) {
        return this.tx(session -> session.get(Task.class, id));
    }
}
