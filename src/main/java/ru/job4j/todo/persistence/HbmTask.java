package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
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
            Task model = new Task();
            model.setId(id);
            session.delete(model);
            return true;
        });
    }

    @Override
    public List<Task> findAll() {
        return this.tx(session -> session.createQuery("from Task").list());
    }

    @Override
    public List<Task> findByName(String name) {
        return this.tx(session -> session.createQuery("from Task where description = :description")
                .setParameter("description", name).list()
        );
    }

    @Override
    public Task findById(int id) {
        return this.tx(session -> session.get(Task.class, id));
    }
}
