package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import ru.job4j.todo.model.User;

import java.util.List;

/**
 * Class HbmUser.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 27.12.2020
 */
@Slf4j
public class HbmUser implements Store<User> {
    private static final HbmUser STORE = new HbmUser();

    public static HbmUser getStore() {
        return STORE;
    }

    @Override
    public User add(User model) {
        this.tx(session -> session.save(model));
        return model;
    }

    @Override
    public boolean replace(int id, User model) {
        return this.tx(session -> {
            session.update(String.valueOf(id), model);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return this.tx(session -> {
            final User user = new User();
            user.setId(id);
            session.delete(user);
            return true;
        });
    }

    @Override
    public List<User> findAll() {
        return this.tx(session -> session.createQuery("from User").list());
    }

    @Override
    public List<User> findByName(String name) {
        return this.tx(session -> session.createQuery("from User where name = :name")
                .setParameter("name", name).list());
    }

    public User findByEmail(String email) {
        return this.tx(session -> {
            Query query = session.createQuery("from User where email = :email")
                    .setParameter("email", email);
            User user = (User) query.uniqueResult();
            return user != null ? user : new User();
        });
    }

    @Override
    public User findById(int id) {
        return this.tx(session -> session.get(User.class, id));
    }
}

