package ru.job4j.todo.persistence;

import org.junit.Test;
import ru.job4j.todo.model.Task;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Class HbmTaskTest.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
public class HbmTaskTest {

    @Test
    public void whenCheckAddAndFindByIdMethods() {
        HbmTask hbmTask = new HbmTask();
        Task task = new Task("Первое задание!!!");
        task = hbmTask.add(task);
        assertThat(hbmTask.findById(task.getId()).getDescription(), is("Первое задание!!!"));
    }

    @Test
    public void replace() {
    }

    @Test
    public void whenDeleteTask() {
        HbmTask hbmTask = new HbmTask();
        Task task = new Task("Удалить");
        task = hbmTask.add(task);
        hbmTask.delete(task.getId());
        assertThat(hbmTask.findById(task.getId()), is(new Task()));
    }

    @Test
    public void whenFindByNameOneTask() {
        HbmTask hbmTask = new HbmTask();
        Task task = new Task("Поиск по имени");
        task = hbmTask.add(task);
        List<Task> expected = Arrays.asList(new Task("Поиск по имени"));
        assertThat(hbmTask.findByName("Поиск по имени").get(0).getDescription(), is(expected.get(0).getDescription()));
        hbmTask.delete(task.getId());
    }
}