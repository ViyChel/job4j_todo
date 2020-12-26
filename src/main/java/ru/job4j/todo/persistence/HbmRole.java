package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.todo.model.Role;

import java.util.ArrayList;
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
    private final SessionFactory sf = ConnectorDB.getInstance();

    public static HbmRole getStore() {
        return STORE;
    }

    @Override
    public Role add(Role model) {
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
    public boolean replace(int id, Role model) {
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
            Role model = new Role();
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
    public List<Role> findAll() {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from ru.job4j.todo.model.Role").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Role> findByName(String name) {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery(
                    "FROM ru.job4j.todo.model.Role WHERE name = :name"
            ).setParameter("name", name).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Role findById(int id) {
        Role result = new Role();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Role item = session.get(Role.class, id);
            if (item != null) {
                result = item;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
