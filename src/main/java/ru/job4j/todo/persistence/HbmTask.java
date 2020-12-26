package ru.job4j.todo.persistence;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
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
    private final SessionFactory sf = ConnectorDB.getInstance();

    public static HbmTask getStore() {
        return STORE;
    }

    @Override
    public Task add(Task model) {
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
    public boolean replace(int id, Task model) {
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
            Task model = new Task();
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
    public List<Task> findAll() {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from ru.job4j.todo.model.Task").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Task> findByName(String name) {
        List result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery(
                    "FROM ru.job4j.todo.model.Task WHERE description = :description"
            ).setParameter("description", name).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Task findById(int id) {
        Task result = new Task();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Task item = session.get(Task.class, id);
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
