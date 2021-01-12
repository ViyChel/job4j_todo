package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Category;

import java.util.List;

/**
 * Class HbmCategory.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 09.01.2021
 */
public class HbmCategory implements Store<Category> {
    private static final HbmCategory STORE = new HbmCategory();

    public static HbmCategory getStore() {
        return STORE;
    }

    @Override
    public Category add(Category model) {
        this.tx(session -> session.save(model));
        return model;
    }

    @Override
    public boolean replace(int id, Category model) {
        return this.tx(session -> {
            session.update(String.valueOf(id), model);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return this.tx(session -> {
            Category model = new Category();
            model.setId(id);
            session.delete(model);
            return true;
        });
    }

    @Override
    public List<Category> findAll() {
        return this.tx(session -> session.createQuery("from Category", Category.class).list());
    }

    @Override
    public List<Category> findByName(String name) {
        return this.tx(session -> session.createQuery("from Category where name = :name", Category.class)
                .setParameter("name", name).list()
        );
    }

    @Override
    public Category findById(int id) {
        return this.tx(session -> session.get(Category.class, id));
    }
}
