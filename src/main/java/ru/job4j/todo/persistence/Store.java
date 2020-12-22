package ru.job4j.todo.persistence;

import java.util.List;

/**
 * Interface Store.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
public interface Store<T> {
    T add(T model);

    boolean replace(int id, T model);

    boolean delete(int id);

    List<T> findAll();

    List<T> findByName(String key);

    T findById(int id);
}
