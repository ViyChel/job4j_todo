package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Function;

/**
 * Interface Store.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
public interface Store<T> {
    SessionFactory SESSION_FACTORY = ConnectorDB.getInstance();

    T add(T model);

    boolean replace(int id, T model);

    boolean delete(int id);

    List<T> findAll();

    List<T> findByName(String name);

    T findById(int id);

    default <T> T tx(final Function<Session, T> command) {
        try (final Session session = SESSION_FACTORY.openSession()) {
            final Transaction tx = session.beginTransaction();
            try {
                T rsl = command.apply(session);
                tx.commit();
                return rsl;
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }
}


