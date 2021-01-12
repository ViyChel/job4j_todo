package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Task.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "description"})
@Table(name = "items")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date(System.currentTimeMillis());
    private boolean done;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Category> categories = new ArrayList<>();

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
