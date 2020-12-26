package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
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
    private final SessionFactory sf = ConnectorDB.getInstance();

    public static HbmUser getStore() {
        return STORE;
    }

    @Override
    public User add(User model) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(model);
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return model;
    }

    @Override
    public boolean replace(int id, User model) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            model.setId(id);
            session.update(model);
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            User model = new User();
            model.setId(id);
            session.delete(model);
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from ru.job4j.todo.model.User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<User> findByName(String name) {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery(
                    "FROM ru.job4j.todo.model.User WHERE name = :name"
            ).setParameter("name", name).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public User findByEmail(String email) {
        User result = new User();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            User user = (User) session.createQuery("FROM ru.job4j.todo.model.User WHERE email = :email")
                    .setParameter("email", email).getSingleResult();
            if (!user.getId().equals(0)) {
                result = user;
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public User findById(int id) {
        User result = new User();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                result = user;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }
}

