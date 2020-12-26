package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private boolean done;
    @NonNull
    private Integer userId;
}
