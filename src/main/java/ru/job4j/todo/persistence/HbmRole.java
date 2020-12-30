package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.todo.model.Role;

import java.util.List;

/**
 * Class HbmRole.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.12.2020
 */
@Slf4j
public class HbmRole implements Store<Role> {
    private static final HbmRole STORE = new HbmRole();
//    private final SessionFactory sf = ConnectorDB.getInstance();

    public static HbmRole getStore() {
        return STORE;
    }

    @Override
    public Role add(Role model) {
        this.tx(session -> session.save(model));
        return model;
    }

    @Override
    public boolean replace(int id, Role model) {
        return this.tx(session -> {
            session.update(String.valueOf(id), model);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return this.tx(session -> {
            final Role role = new Role();
            role.setId(id);
            session.delete(role);
            return true;
        });
    }

    @Override
    public List<Role> findAll() {
        return this.tx(session -> session.createQuery("from Role").list());
    }

    @Override
    public List<Role> findByName(String name) {
        return this.tx(session -> session.createQuery("from Role where name = :name")
                .setParameter("name", name).list());
    }

    @Override
    public Role findById(int id) {
        return this.tx(session -> session.get(Role.class, id));
    }
}
